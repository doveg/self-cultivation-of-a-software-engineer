# MySQL

---

## 日志

---

### MySQL 的日志

MySQL 的日志有很多种，如：binlog / 二进制日志、error log / 错误日志、查询日志、慢查询日志等。

此外 InnoDB 存储引擎还提供了两种事务日志：**redo log（重做日志）**、**undo log（回滚日志）**。

其中 **redo log 用于保证事务持久性；undo log 则是事务原子性和隔离性实现的基础**。

bin log：

bin log 是 MySQL 数据库级别的文件，记录对 MySQL 数据库执行修改的所有操作，不会记录 select 和 show 语句，主要用于恢复数据库和同步数据库。

redo log：

redo log 是 InnoDB 引擎级别，用来记录 InnoDB 存储引擎的事务日志，不管事务是否提交都会记录下来，用于数据恢复。当数据库发生故障，InnoDB 存储引擎会使用 redo log
恢复到发生故障前的时刻，以此来保证数据的完整性。将参数 innodb_flush_log_at_tx_commit 设置为 1，那么在执行 commit 时会将 redo log 同步写到磁盘。

undo log：

除了记录 redo log 外，当进行数据修改时还会记录 undo log，undo log 用于数据的撤回操作，它保留了记录修改前的内容。通过 undo log 可以实现事务回滚，并且可以根据
undo log 回溯到某个特定的版本的数据，实现 MVCC。

### redo log 与 binlog 的区别

都可以记录写操作并用于数据的恢复。

**1. 作用不同：**

redo log 是用于 crash recovery 的，保证 MySQL 宕机也不会影响持久性；

binlog 是用于 point-in-time recovery 的，保证服务器可以基于时间点恢复数据，此外 binlog 还用于主从复制。

**2. 层次不同：**

redo log 是 InnoDB 存储引擎实现的；

binlog 是 MySQL 的服务器层（可以参考文章前面对 MySQL 逻辑架构的介绍）实现的，同时支持 InnoDB 和其他存储引擎。

**3. 内容不同：**

redo log 是物理日志，内容基于磁盘的 Page；

binlog 的内容是二进制的，根据 binlog_format 参数的不同，可能基于 sql 语句、基于数据本身或者二者的混合。

**4. 写入时机不同：**

binlog 在事务提交时写入；

redo log 的写入时机相对多元：

> 当事务提交时会调用 fsync 对 redo log 进行刷盘，这是默认情况下的策略，修改 innodb_flush_log_at_trx_commit 参数可以改变该策略，但事务的持久性将无法保证。
>
> 除了事务提交时，还有其他刷盘时机：如 master thread 每秒刷盘一次 redo log 等，这样的好处是不一定要等到 commit 时刷盘，commit 速度大大加快。







---
