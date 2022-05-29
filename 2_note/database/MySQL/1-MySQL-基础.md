# MySQL

---

## 基础部分

---

### 三大范式

视频讲解：[数据库三范式，不是你想的那样](https://www.bilibili.com/video/BV1ZS4y1a7jG)

**1. 第一范式 / 1NF：**

**字段都具备原子性。表需要有一个主键。**

数据库表中不能出现重复记录，每个字段是原子性的不能再分。

> 比如：在学生表中，联系方式这个字段出现了邮件和电话号码一起存，用逗号连接的情况。一个值里不能同时包含两种数据，因为会违反第一范式。

**2. 第二范式 / 2NF：**

**满足第一范式，非主键字段完全依赖主键，不能产生部分依赖。**

第二范式是建立在第一范式基础上的，另外要求所有非主键字段完全依赖主键，不能产生部分依赖。

> 比如：在学生表中，增加老师名字这个字段，但是老师的名字信息不依赖于表的主键，违反了第二范式。

**3. 第三范式 / 3NF：**

**满足第二范式，非主键字段不能依赖于其他非主键字段，非主键字段和主键之间不能产生传递依赖。**

建立在第二范式基础上的，非主键字段不能传递依赖于主键字段。

> 比如：在学生表中，增加班级编号、班级名称这两个字段。班级名称依赖于班级编号，班级编号依赖于学生表的主键，产生了传递依赖。

**4. 逆范式：**

**通过增加冗余或重复的数据来提高数据库的读性能**。

### 什么是分区表

分区表是一个独立的逻辑表，但是底层由多个物理子表组成。

当查询条件的数据分布在某一个分区的时候，查询引擎只会去某一个分区查询，而不是遍历整个表。

在管理层面，如果需要删除某一个分区的数据，只需要删除对应的分区即可。

### 分区表类型

按照范围分区。

在 /var/lib/mysql/data/ 可以找到对应的数据文件，每个分区表都有一个使用 #分隔命名的表文件。

list 分区：

> 对于 List 分区，分区字段必须是已知的，如果插入的字段不在分区时枚举值中，将无法插入。

hash 分区：

> 可以将数据均匀地分布到预先定义的分区中。



---

## SQL 关键字

---

### exist 和 in 的区别

exists 用于对外表记录做筛选。exists 会遍历外表，将外查询表的每一行，代入内查询进行判断。当 exists 里的条件语句能够返回记录行时，条件就为真，返回外表当前记录。反之如果
exists 里的条件语句不能返回记录行，条件为假，则外表当前记录被丢弃。

select a.* from A awhere exists(select 1 from B b where a.id=b.id)

in 是先把后边的语句查出来放到临时表中，然后遍历临时表，将临时表的每一行，代入外查询去查找。

select * from Awhere id in(select id from B)

子查询的表比较大的时候，使用 exists 可以有效减少总的循环次数来提升速度；当外查询的表比较大的时候，使用 in 可以有效减少对外查询表循环遍历来提升速度。

### MySQL 中使用 in 会不会走索引

如果你 source 字段是一个 unique，in 肯定会用到索引。

如果 source 字段来来去去就那么十几个值，这种情况下影响结果集巨大，就会全表扫描。这种情况全表扫描还要快于利用索引，只要理解索引的本质不难明白 MySQL 为何不使用索引。

### truncate、delete 与 drop 区别

相同点：

> truncate 和不带 where 子句的 delete、以及 drop 都会删除表内的数据。
>
> drop、truncate 都是 DDL 语句（数据定义语言），执行后会自动提交。

不同点：

> truncate 和 delete 只删除数据不删除表的结构；drop 语句将删除表的结构被依赖的约束、触发器、索引；
>
> 一般来说，执行速度: drop > truncate > delete。

### where 和 having 的区别

**where 在数据分组前进行过滤，having 在数据分组后进行过滤。**

where 子句作用于表和视图，having 作用于组。

视频讲解：[WHERE 和 HAVING 以及 ON 的区别](https://www.bilibili.com/video/BV1ma411h7PP)



---

## 语句执行流程

---

### 语句执行流程

视频讲解：[MySQL 执行过程（一条 SQL 从 Java 到数据库都经历了什么）](https://www.bilibili.com/video/BV1Sq4y1377k)

视频讲解：[一条查询语句的执行过程](https://www.bilibili.com/video/BV1NP4y1F7G5)

1. 客户端提交 SQL（使用连接池的连接，通过数据库驱动访问服务端）
2. 服务端获得 SQL
3. 解析器解析 SQL，生成解析树
4. 优化器优化 SQL（执行计划缓存）
5. 执行器从数据库引擎获取数据（数据缓存）
6. 服务端返回查询结果

### 查询语句执行流程

查询语句的执行流程如下：权限校验、查询缓存、分析器、优化器、权限校验、执行器、引擎。

举个例子，查询语句如下：

select * from user where id > 1 and name = '条件';

1、首先检查权限，没有权限则返回错误；

2、MySQL8.0 以前会查询缓存，缓存命中则直接返回，没有则执行下一步；

3、词法分析和语法分析。提取表名、查询条件，检查语法是否有错误；

4、两种执行方案，先查 id > 1 还是 name = '条件'，优化器根据自己的优化算法选择执行效率最好的方案；

5、校验权限，有权限就调用数据库引擎接口，返回引擎的执行结果。

### 更新语句执行过程

更新语句执行流程如下：分析器、权限校验、执行器、引擎、redo log（prepare 状态）、binlog、redo log（commit 状态）

举个例子，更新语句如下：

update user set name = '条件' where id = 1;

1、先查询到 id 为 1 的记录，有缓存会使用缓存。

2、拿到查询结果，将 name 更新为条件，然后调用引擎接口，写入更新数据，innodb 引擎将数据保存在内存中，同时记录 redo log，此时 redo log 进入 prepare 状态。

3、执行器收到通知后记录 binlog，然后调用引擎接口，提交 redo log 为 commit 状态。

4、更新完成。

为什么记录完 redo log，不直接提交，而是先进入 prepare 状态：

> 假设先写 redo log 直接提交，然后写 binlog，写完 redo log 后，机器挂了，binlog 日志没有被写入，
>
> 那么机器重启后，这台机器会通过 redo log 恢复数据，但是这个时候 binlog 并没有记录该数据，
>
> 后续进行机器备份的时候，就会丢失这一条数据，同时主从同步也会丢失这一条数据。




---

## 参考链接

---

- [MySQL 官方文档——幻影行](https://dev.mysql.com/doc/refman/8.0/en/innodb-next-key-locking.html)
- [深入学习 MySQL 事务：ACID 特性的实现原理](https://www.cnblogs.com/kismetv/p/10331633.html)
- [mysql 锁机制的再研究 ](https://mp.weixin.qq.com/s/FJKRUyGUNgDYRvPAy20x3w)
- [MySQL 中幻读出现的原因及解决方案](https://blog.csdn.net/nandao158/article/details/116007366)
- [MySQL的锁机制 - 记录锁、间隙锁、临键锁](https://zhuanlan.zhihu.com/p/48269420)
- [Innodb 锁机制：Next-Key Lock 浅谈](https://www.cnblogs.com/zhoujinyi/p/3435982.html)
- [MySQL next-key lock 加锁范围是什么？](https://segmentfault.com/a/1190000040129107)
- [间隙锁和 next-key lock](https://www.jianshu.com/p/d1aba64b5c03)
- [MySQL 教程（十）---MySQL ACID 实现原理](https://www.lixueduan.com/post/mysql/10-acid/)
- [Innodb 中的事务隔离级别和锁的关系](https://tech.meituan.com/2014/08/20/innodb-lock.html)
- [MySQL 设计之三范式](https://segmentfault.com/a/1190000022843792)
- [MySQL 是如何保证一致性、原子性和持久性的](https://cloud.tencent.com/developer/article/1600883)
- [MySQL 索引那些事：什么是索引？为什么加索引就查得快了？](http://blog.itpub.net/70000181/viewspace-2776159/)
- [MySQL 索引类型、索引原理、索引分析与优化、慢查询优化](http://fishleap.top/pages/c62f50/)
- [MySQL 探秘（三）MySQL 索引原理](https://princeli.com/mysql%E6%8E%A2%E7%A7%98%E4%B8%89mysql%E7%B4%A2%E5%BC%95%E5%8E%9F%E7%90%86/)
- [MySql 创建索引原则](https://developer.aliyun.com/article/6719)
- [mysql 索引失效的场景](https://blog.csdn.net/vtopqx/article/details/112176732)
- [MySQL 索引连环 18 问](https://www.dockone.io/article/2434410)
- [解决 hash 冲突的三个方法](https://www.cnblogs.com/wuchaodzxx/p/7396599.html)
- [解决哈希冲突的常用方法分析](https://cloud.tencent.com/developer/article/1672781)
- [MySQL 经典 36 问](https://mp.weixin.qq.com/s/UYytwy46FZz3HWZmhC_X4g)
- [B + 树叶子结点到底存储了什么？](https://blog.csdn.net/Alice_8899/article/details/105357902)
- [五分钟搞懂 MySQL 索引下推](https://juejin.cn/post/7005794550862053412)
- [mysql 数据库调优](https://blog.csdn.net/weixin_39564831/article/details/113457726)
- []()
- []()
- []()
- []()
- []()

---
