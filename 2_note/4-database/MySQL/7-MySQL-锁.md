# MySQL

---

## 锁

---

### 锁的作用范围分类

由于加锁本身需要消耗资源（获得锁、检查锁、释放锁等都需要消耗资源），因此在锁定数据较多情况下使用表锁可以节省大量资源。

MySQL 中不同的存储引擎支持的锁是不一样的，例如 MyIsam 只支持表锁，而 InnoDB 同时支持表锁和行锁，且出于性能考虑，绝大多数情况下使用的都是行锁。

**1. 全局锁：**

在 DB 级别对整个数据库实例加锁。

- 加锁表现：

> 数据库处于只读状态。
>
> 阻塞对数据的增删改以及 DDL。

- 加锁方式：lock Flush tables with read lock

- 释放锁：unlock tables（发生异常时会自动释放）

- 作用场景：

> 全局锁主要用于做全库的逻辑备份，和设置数据库只读（set global readonly=true）相比，全局锁在发生异常时会自动释放

**2. 表锁：**

表级别对操作的整张表加锁，锁定颗粒度大，资源消耗少，不会出现死锁，但并发度低。

分为表共享锁和表排他锁，注意：意向锁为表锁，但是由存储引擎自己维护，无需用户手工命令干预。

- 显示加锁方式：lock tables {tb_name} read/write

- 释放锁：unlock table {tb_name}（连接中断也会自动释放）

**3. 行锁：**

InnoDB 支持行级别锁，锁粒度小并发度高，但是加锁开销大也很可能会出现死锁

InnoDB 行锁住的是索引项，注意当回表时，主键的聚簇索引也会加上锁。

- 加锁方式：

> 普通 select… 查询（不加锁）。
>
> 普通 insert、update、delete…（隐式加写锁）。
>
> select…lock in share mode（加读锁）。
>
> select…for update（加写锁）。

- 解锁：

> 提交 / 回滚事务（commit/rollback） kill 阻塞进程

行锁用的最多且更容易出现死锁问题。

### 锁的模式分类

我们常规理解的锁分为 2 大类：**读锁 / 共享锁（S）、写锁 / 排他锁（X）。**

两把锁之间的兼容性说明如下表：

| 兼容性 | X | S |
| :---: | :---: | :---: |
| X | 0 | 0 |
| S | 0 | 1 |

横轴表示已持有的锁，纵轴表示尝试获取的锁。

1 表示成功（即兼容，表现为正常进行下一步操作），0 表示失败（即冲突，表现为阻塞住当前操作）

排他锁和任何锁均不兼容。

数据库识别锁的冲突，最容易想到的识别方案就是遍历：

> step1：判断表是否已被其他事务用表锁锁住。
>
> step2: 判断表中的每一行是否已被行锁锁住。
>
> 其中 step2 需要遍历整个表，效率在数据库是没法接受的，因此 innodb 使用意向锁来解决这个问题。

Innodb 实现方案：

> T1 需要先申请表的意向共享锁 IS，成功后再申请一行的行锁 S。
>
> 注意：**意向共享锁是表级锁，由存储引擎自己维护，无需用户命手工命令干预**
>
> 在意向锁存在的情况下，上面的判断可以改为：
>
> step1：判断表是否已被其他事务用表锁锁住。
>
> step2: 发现表上有意向共享锁，说明表中行被共享行锁锁住了，因此，事务 B 申请表的写锁被阻塞。

此时就引入的意向锁，加入意向锁后，锁的兼容性分析如下表：

| 兼容性 | IX | IS | X | S |
| :---: | :---: | :---: | :---: | :---: |
| IX | 1 | 1 | 0 | 0 |
| IS | 1 | 1 | 0 | 1 |
| X | 0 | 0 | 0 | 0 |
| S | 0 | 1 | 0 | 1 |

### 共享锁和排他锁

SELECT 的读取锁定主要分为两种方式：共享锁和排他锁。

共享锁：

select * from table where id<6 **lock in share mode**;

排他锁：

select * from table where id<6 **for update**;

这两种方式主要的不同在于 **LOCK IN SHARE MODE** 多个事务同时更新同一个表单时很容易造成死锁。

申请排他锁的前提是，没有线程对该结果集的任何行数据使用排它锁或者共享锁，否则申请会受到阻塞。在进行事务操作时，MySQL
会对查询结果集的每行数据添加排它锁，其他线程对这些数据的更改或删除操作会被阻塞（只能读操作），直到该语句的事务被 commit 语句或 rollback 语句结束为止。

SELECT... FOR UPDATE 使用注意事项：

> for update 仅适用于 InnoDB，且必须在事务范围内才能生效。
>
> 根据主键进行查询，查询条件为 like 或者不等于，主键字段产生表锁。
>
> 根据非索引字段进行查询，会产生表锁。

### InnoDB 中的行锁（重点）

**1. Record Lock / 记录锁**：

**行锁，单行记录上的锁，作用于索引记录。**

例如：SELECT id FROM t WHERE id=1 FOR UPDATE

**2. Gap Lock / 间隙锁**：

**锁定一个范围，但不包括记录本身。**
间隙锁是为了防止同一事务的两次当前读，出现幻读。

间隙锁是一种加在两个索引之间的锁（众所周知索引是有序的），或者加在第一个索引之前，或最后一个索引之后的间隙。

间隙锁锁住的是一个区间，而不仅仅是这个区间中的每一条数据。

间隙锁只阻止其他事务插入到间隙中，他们不阻止其他事务在同一个间隙上获得间隙锁，所以 gap x lock 和 gap s lock 有相同的作用。

**3. Next-Key Lock / 临键锁**：

**相当于 Record Lock 加上 Gap Lock。不仅会锁定记录本身，还会锁定一个范围。**
对于行的查询，都是采用该方法，主要目的是解决幻读的问题。

**4. Insert Intention / 插入意向锁：**

该锁只会出现在 insert 操作执行前（并不是所有 insert 操作都会出现），目的是为了提高并发插入能力，注意虽有意向二字，但插入意向锁是行锁。

**插入意向锁是在插入一行记录操作之前设置的一种特殊的间隙锁，这个锁释放了一种插入方式的信号，**

**亦即多个事务在相同的索引间隙插入时，如果不是插入间隙中相同的位置就不需要互相等待。**

> 普通的 Gap Lock **不允许在**（上一条记录，本记录） 范围内插入数据
>
> 插入意向锁 Gap Lock **允许在**上一条记录，本记录） 范围内插入数据

假设有索引值 4，7，几个不同的事务准备插入 5，6，每个锁都在获得插入行的独占锁之前用插入意向锁各自锁住了 4，7 之间的间隙，但是不阻塞对方不冲突的插入行。

锁类型兼容矩阵，横轴表示已持有的锁，纵轴表示尝试获取的锁。1 表示成功（即兼容，表现为正常进行下一步操作），0 表示失败（即冲突，表现为阻塞住当前操作）：

| 兼容性 | Gap | Insert Intention | Record | Next-Key |
| :---: | :---: | :---: | :---: | :---: |
| Gap | 1 | 1 | 1 | 1 |
| Insert Intention | 0 | 1 | 1 | 0 |
| Record | 1 | 1 | 0 | 0 |
| Next-Key | 1 | 1 | 0 | 0 |

### 间隙锁产生的条件（重点）

1、间隙锁只有在事务隔离级别 RR 中才会产生；

2、唯一索引只有锁住多条记录或者一条不存在的记录的时候，才会产生间隙锁，指定给某条存在的记录加锁的时候，只会加记录锁，不会产生间隙锁；

3、普通索引不管是锁住单条，还是多条记录，都会产生间隙锁；

RR 事务隔离级别下：

1、使用普通索引锁定；

2、使用多列唯一索引；

3、使用唯一索引锁定多行记录。

对于使用唯一索引来搜索并给某一行记录加锁的语句，不会产生间隙锁。（这不包括搜索条件仅包括多列唯一索引的一些列的情况；在这种情况下，会产生间隙锁。）例如，如果id列具有唯一索引，则下面的语句仅对具有id值100的行使用记录锁，并不会产生间隙锁：

innodb_locks_unsafe_for_binlog：默认值为OFF，即启用间隙锁。

因为此参数是只读模式，如果想要禁用间隙锁，需要修改 my.cnf（windows是my.ini） 重新启动才行。

### 临键锁产生的条件（重点）

临键锁的主要目的，也是为了避免幻读（Phantom Read），如果把事务的隔离级别降级为RC，临键锁则也会失效。

### 锁组合

锁的模式：

> lock_s / 读锁，共享锁。
>
> lock_x / 写锁，排它锁。

锁的类型：

> Record_Lock ：锁记录。
>
> Gap_Lock ：锁记录前的 GAP。
>
> Next-Key Lock ：同时锁记录 + 记录前的 GAP。
>
> insert_Intention_Lock ：插入意向锁，其实是特殊的 GAP 锁。

锁模型可以和锁类型任意组合，如：

> locks gap before rec，表示为 gap 锁：lock->type_mode & LOCK_GAP
>
> locks rec but not gap，表示为记录锁，非 gap 锁：lock->type_mode & LOCK_REC_NOT_GAP
>
> insert intention，表示为插入意向锁：lock->type_mode & LOCK_INSERT_INTENTION
>
> waiting，表示锁等待：lock->type_mode & LOCK_WAIT

在 mysql 源码中使用了 uint32 类型来表示锁，最低的 4 个 bit 表示 lock_mode，5-8 bit 表示 lock_type（目前只用了 5 和 6 位，大小为 16 和 32
，表示 LOCK_TABLE 和 LOCK_REC），剩下的高位 bit 表示行锁的类型 record_lock_type。

### 悲观锁和乐观锁

**悲观锁**：

指的是对数据被外界（包括本系统当前的其他事务，以及来自外部系统的事务处理）修改持保守态度，因此，在整个数据处理过程中，将数据处于锁定状态。

悲观锁的实现，往往依靠数据库提供的锁机制（也只有数据库层提供的锁机制才能真正保证数据访问的排他性，否则，即使在本系统中实现了加锁机制，也无法保证外部系统不会修改数据）。

在悲观锁的情况下，为了保证事务的隔离性，就需要一致性锁定读。**读取数据时给加锁**，其它事务无法修改这些数据。修改删除数据时也要加锁，其它事务无法读取这些数据。

悲观锁大多数情况下依靠数据库的锁机制实现，以保证操作最大程度的独占性。但随之而来的就是数据库性能的大量开销，特别是对长事务而言，这样的开销往往无法承受。

**乐观锁**：

乐观锁机制采取了更加宽松的加锁机制。**大多是基于数据版本（Version）记录机制实现**。

数据版本：

> 为数据增加一个版本标识，在基于数据库表的版本解决方案中，一般是通过为数据库表增加一个 “version” 字段来实现。

实现过程：

> 读取出数据时，将此版本号一同读出，之后更新时，对此版本号加一。此时，将提交数据的版本数据与数据库表对应记录的当前版本信息进行比对，如果提交的数据版本号大于数据库表当前版本号，则予以更新，否则认为是过期数据。







---
