# MySQL

---

## 事务

---

### 事务的特性 / ACID

**1. 原子性 / Atomicity：**

保证每个事务都被视为一个单独的单元，要么完全成功，要么完全失败。一个事务中的所有操作，**要么全部执行成功，要么全部不执行**。

**2. 一致性 / Consistency：**

事务执行结束后，**数据库的完整性约束**没有被破坏，**事务执行的前后都是合法的数据状态**。

数据库的完整性约束包括但不限于：实体完整性（如行的主键存在且唯一）、列完整性（如字段的类型、大小、长度要符合要求）、外键约束、用户自定义完整性（如转账前后，两个账户余额的和应该不变）。

**3. 隔离性 / Isolation：**

事务内部的操作及使用的数据对正在进行的其他事务是隔离的，**并发执行的各个事务之间不能互相干扰**。隔离性可以防止多个事务并发执行时由于交叉执行而导致数据的不一致。

**4. 持久性 / Durability：**

事务一旦提交，**对数据库的改变就应该是永久性的**。接下来的其他操作或故障不应该对其有任何影响。


---

## 隔离级别

---

### 事务的隔离级别（重点）

**1. 读未提交 / Read Uncommitted：**

可以读取到其他会话中未提交事务修改的数据。读未提交可能出现脏读。

**脏读：在事务中读到了其他会话未提交的数据。**

**2. 读已提交 / Read Committed（RC）：**

只能读取到已经提交的数据。Oracle 等多数数据库默认都是该级别。读已提交可能出现不可重复读。

**不可重复读：在事务中读到了其他会话已经提交的数据。**

**3. 可重复读 / Repeated Read（RR）：**

在同一个事务内的**查询**都是事务开始时刻一致的，InnoDB 默认级别。在 SQL 标准中，该隔离级别消除了不可重复读。可重复读可能出现幻读。

**幻读：在事务中使用当前读，读到了其他会话提交的新数据。**

**4. 串行化 / Serializable：**

完全串行化的读，每次读都需要获得表级共享锁，读写相互都会阻塞。串行化没有幻读，但实际中因为性能等原因不会采用这种级别。

### 幻读 / Phantom Read（重点）

**幻读**：是幻影行 / Phantom Rows 产生的负面结果，快照读和当前读一起使用就能复现。

幻读典型场景复现：

> 事务 A 按条件读取数据时，事务 B 插入了相同条件的新数据，事务 A 再次按原先条件进行读取操作**修改**时，读取到了事务 B 插入的新数据。
>
> 事务 C 查不出事务 D 新增的记录，但是自身插入相同主键的记录会报主键冲突。

出现的原因：

> 如果事务中都是用快照读，就不会产生幻读，**快照读和当前读一起使用的时候可能产生幻读**。

解决办法：

> 为了防止幻读，InnoDB 采用了 **Next-Key Lock** 算法，将记录锁与间隙锁相结合。

### 快照读和当前读（重点）

**快照读 / 非加锁读 / 一致性读：**

读取开启事务时的版本数据。

> 例如：使用普通的 select 语句，这种情况下使用 MVCC 避免了脏读、不可重复读、幻读，保证了隔离性。

InnoDB 提供的非锁定读，不需要等待访问行上的锁释放，读取行的一个快照。

快照读情况下，InnoDB 通过 mvcc 机制避免了幻读现象。

而 mvcc 机制无法避免当前读情况下出现的幻读现象。因为当前读每次读取的都是最新数据，这时如果两次查询中间有其它事务插入数据，就会产生幻读。

**当前读 / 加锁读：**

读取数据库当前版本数据。

**加锁读在查询时会对查询的数据加锁**（共享锁或排它锁）。

由于锁的特性，当某事务对数据进行加锁读后，其他事务无法对数据进行写操作，因此可以避免脏读和不可重复读。而避免幻读，则需要通过 next-key lock。

因此，加锁读同样可以避免脏读、不可重复读和幻读，保证隔离性。

**共享锁读取的查询语句：select...lock in share mode**

**排它锁读取的查询语句：select...for update**

所以，select 执行的是快照读（某个版本的数据，Read View），而 update 执行的是当前读（最新的数据，即最新的 Read View）。

### MySQL 如何避免幻读

在快照读情况下，MySQL 通过 mvcc 来避免幻读。

在当前读情况下，MySQL 通过 next-key 来避免幻读（加行锁和间隙锁来实现的）。

> next-key 包括两部分：行锁和间隙锁。行锁是加在索引上的锁，间隙锁是加在索引之间的。
>
> Serializable 隔离级别也可以避免幻读，会锁住整张表，并发性极低，一般不会使用。





---

## MVCC

---

### MVCC 的实现原理

**MVCC / Multi-version concurrency control / 多版本并发控制：**

同一份数据保留多版本的一种方式，进而实现并发控制。在查询的时候，通过 read view 和版本链找到对应版本的数据。

作用：

**提升并发性能，对于高并发场景，MVCC 比行级锁开销更小。**

MVCC 的实现依赖于版本链，版本链是通过表的三个隐藏字段实现。

> DB_TRX_ID：当前事务 id，通过事务 id 的大小判断事务的时间顺序。
>
> DB_ROLL_PRT：回滚指针，指向当前行记录的上一个版本，通过这个指针将数据的多个版本连接在一起构成 undo log 版本链。
>
> DB_ROLL_ID：主键，如果数据表没有主键，InnoDB 会自动生成主键。

使用事务更新行记录的时候，就会生成版本链，执行过程如下：

> 用排他锁锁住该行；
>
> 将该行原本的值拷贝到 undo log，作为旧版本用于回滚；
>
> 修改当前行的值，生成一个新版本，更新事务 id，使回滚指针指向旧版本的记录，这样就形成一条版本链。

read view 可以理解成将数据在每个时刻的状态拍成 “照片” 记录下来。在获取某时刻 t 的数据时，到 t 时间点拍的 “照片” 上取数据。

在 read view 内部维护一个活跃事务链表，表示生成 read view 的时候还在活跃的事务。这个链表包含在创建 read view 之前还未提交的事务，不包含创建 read view
之后提交的事务。

不同隔离级别创建 read view 的时机不同：

> read committed：每次执行 select 都会创建新的 read_view，保证能读取到其他事务已经提交的修改。
>
> repeatable read：在一个事务范围内，第一次 select 时更新这个 read_view，以后不会再更新，后续所有的 select 都是复用之前的 read_view。这样可以保证事务范围内每次读取的内容都一样，即可重复读。

read view 的记录筛选方式：

前提：DATA_TRX_ID 表示每个数据行的最新的事务 ID；up_limit_id 表示当前快照中的最先开始的事务；low_limit_id 表示当前快照中的最慢开始的事务，即最后一个事务。

> 如果 DATA_TRX_ID < up_limit_id：说明在创建 read view 时，修改该数据行的事务已提交，该版本的记录可被当前事务读取到。
>
> 如果 DATA_TRX_ID >= low_limit_id：说明当前版本的记录的事务是在创建 read view 之后生成的，该版本的数据行不可以被当前事务访问。
>
> 此时需要通过版本链找到上一个版本，然后重新判断该版本的记录对当前事务的可见性。
>
> 如果 up_limit_id <= DATA_TRX_ID < low_limit_i：
>
> > 需要在活跃事务链表中查找是否存在 ID 为 DATA_TRX_ID 的值的事务。
> >
> > 如果存在，因为在活跃事务链表中的事务是未提交的，所以该记录是不可见的。此时需要通过版本链找到上一个版本，然后重新判断该版本的可见性。
> >
> > 如果不存在，说明事务 trx_id 已经提交了，这行记录是可见的。

总结：

**InnoDB 的 MVCC 是通过 read view 和版本链实现，版本链保存有历史版本记录，通过 read view 判断当前版本的数据是否可见，**

**如果不可见，再从版本链中找到上一个版本，继续进行判断，直到找到一个可见的版本。**

### MySQL 的 InnoDB 如何实现 MVCC

在 InnoDB 中，会在每行数据后添加两个额外的隐藏的值来实现 MVCC，这两个值一个记录这行数据何时被创建，另外一个记录这行数据何时过期（或者被删除）。

在实际操作中，存储的并不是时间，而是事务的版本号。

**每开启一个新事务，事务的版本号就会递增。**

**通过 MVCC，虽然每行记录都需要额外的存储空间，更多的行检查工作以及一些额外的维护工作，但可以减少锁的使用。**
大多数读操作都不用加锁，读数据操作很简单，性能很好，并且也能保证只会读取到符合标准的行，也只锁住必要行。

根据 MVCC 的定义，并发提交数据时会出现冲突。

在可重复读级别中，通过 MVCC 机制，虽然让数据变得可重复读，但我们读到的数据可能是历史数据，是不及时的数据，不是数据库当前的数据。

事务的隔离级别实际上都是定义了当前读的级别，MySQL 为了减少锁处理（包括等待其它锁）的时间，提升并发能力，引入了快照读的概念，使得 select 不用加锁。

而 update、insert 这些当前读，就需要另外的模块来解决了。为了解决当前读中的幻读问题，MySQL 事务使用了 Next-Key Lock。



---

## 事务特性的实现

---

### MySQL 如何实现原子性

InnoDB 实现回滚，靠的是 undo log，回滚日志是实现原子性的关键，当事务回滚时能够撤销所有已经成功执行的 sql 语句，。。

**回滚日志 / undo log：**

当事务对数据库进行修改时，InnoDB 会生成对应的 undo log；如果事务执行失败或调用了 rollback，导致事务需要回滚，便可以利用 undo log 中的信息将数据回滚到修改之前的样子。

例如：

1. 当你 delete 一条数据的时候，就需要记录这条数据的信息，回滚的时候，insert 这条旧数据
2. 当你 update 一条数据的时候，就需要记录之前的旧值，回滚的时候，根据旧值执行 update 操作
3. 当年 insert 一条数据的时候，就需要这条记录的主键，回滚的时候，根据主键执行 delete 操

### MySQL 如何实现持久性

InnoDB 作为 MySQL 的存储引擎，数据是存放在磁盘中的，但如果每次读写数据都需要磁盘 IO，效率会很低。

为此，InnoDB 提供了**缓存（Buffer Pool）**，Buffer Pool 中包含了磁盘中部分数据页的映射，作为访问数据库的缓冲：

当从数据库读取数据时，会首先从 Buffer Pool 中读取，如果 Buffer Pool 中没有，则从磁盘读取后放入 Buffer Pool； 当向数据库写入数据时，会首先写入 Buffer
Pool，Buffer Pool 中修改的数据会定期刷新到磁盘中（这一过程称为刷脏）。

Buffer Pool 的使用大大提高了读写数据的效率，但是也带了新的问题：如果 MySQL 宕机，而此时 Buffer Pool
中修改的数据还没有刷新到磁盘，就会导致数据的丢失，事务的持久性无法保证。

于是，**redo log** 被引入来解决这个问题：当数据修改时，除了修改 Buffer Pool 中的数据，还会在 redo log 记录这次操作；当事务提交时，会调用 fsync 接口对
redo log 进行刷盘。如果 MySQL 宕机，重启时可以读取 redo log 中的数据，对数据库进行恢复。redo log 采用的是 WAL（Write-ahead
logging，预写式日志），所有修改先写入日志，再更新到 Buffer Pool，保证了数据不会因 MySQL 宕机而丢失，从而满足了持久性要求。

既然 redo log 也需要在事务提交时将日志写入磁盘，为什么它比直接将 Buffer Pool 中修改的数据写入磁盘（即刷脏）要快呢？主要有以下两方面的原因：

> 刷脏是随机 IO，因为每次修改的数据位置随机，但写 redo log 是追加操作，属于顺序 IO。
>
> 刷脏是以数据页（Page）为单位的，MySQL 默认页大小是 16KB，一个 Page 上一个小修改都要整页写入；而 redo log 中只包含真正需要写入的部分，无效 IO 大大减少。

### MySQL 如何实现隔离性

隔离性要求同一时刻只能有一个事务对数据进行写操作，InnoDB 通过**锁机制**来保证这一点。

锁机制的基本原理可以概括为：

> 事务在修改数据之前，需要先获得相应的锁；获得锁之后，事务便可以修改数据；该事务操作期间，这部分数据是锁定的，其他事务如果需要修改数据，需要等待当前事务提交或回滚后释放锁。

InnoDB 实现的 RR，通过锁机制（包含 next-key lock）、MVCC（包括数据的隐藏列、基于 undo log
的版本链、ReadView）等，实现了一定程度的隔离性，可以满足大多数场景的需要。

RR 虽然避免了幻读问题，但是毕竟不是 Serializable，不能保证完全的隔离。

如果在事务中第一次读取采用非加锁读，第二次读取采用加锁读，则如果在两次读取之间数据发生了变化，两次读取到的结果不一样，因为加锁读时不会采用 MVCC。

### MySQL 如何实现一致性

一致性是事务追求的最终目标。原子性、持久性和隔离性，都是为了保证数据库状态的一致性，即数据库的完整性约束。

此外，除了数据库层面的保障，一致性的实现也需要应用层面进行保障。

实现一致性的措施包括：

> 保证原子性、持久性和隔离性，如果这些特性无法保证，事务的一致性也无法保证。
>
> **数据库本身提供保障**，例如不允许向整形列插入字符串值、字符串长度不能超过列的限制等。
>
> **应用层面进行保障**，例如如果转账操作只扣除转账者的余额，而没有增加接收者的余额，无论数据库实现的多么完美，也无法保证状态的一致。





---
