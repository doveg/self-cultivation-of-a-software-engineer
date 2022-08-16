# Oracle

---

## 基础部分

---

### MySQL 和 Oracle 区别

**1、最重要的区别：**

MySQL：轻量型数据库，免费、开源，没有服务恢复数据。

Oracle：重量型数据库，收费，Oracle 公司对 Oracle 数据库有任何服务。

**2、事务隔离级别：**

MySQL：四种隔离级别：**读未提交，读已提交，可重复读，串行化**。

Oracle：两种隔离级别：**读已提交、串行化**。

**3、事务提交：**

MySQL：默认是自动提交。

Oracle：默认不自动提交，需要用户手动提交，需要在写 commit; 指令或者点击 commit 按钮。

**4、对事务的支持：**

MySQL：在 InnoDB 存储引擎的行级锁的情况下才可支持事务。

Oracle：完全支持事务。

**5、逻辑备份：**

MySQL：逻辑备份时要锁定数据，才能保证备份的数据是一致的，影响业务正常的 dml 使用。

Oracle：逻辑备份时不锁定数据，且备份的数据一致。

**6、分页查询：**

MySQL：有 limit 就可以实现分页。

Oracle：需要用到伪列 ROWNUM 和嵌套查询。

**7、并发性：**

MySQL：

- 以表级锁为主，对资源锁定的粒度很大，如果一个 session 对一个表加锁时间过长，会让其他 session 无法更新此表中的数据。
- 虽然 InnoDB 引擎的表可以用行级锁，但这个行级锁的机制依赖于表的索引，如果表没有索引，或者 sql 语句没有使用索引，那么仍然使用表级锁。

Oracle：

- Oracle 使用行级锁，对资源锁定的粒度要小很多，只是锁定 sql 需要的资源，并且加锁是在数据库中的数据行上，不依赖与索引，所以 Oracle 对并发性的支持要好很多

8、数据的持久性：

MySQL：在数据库更新或者重启，则会丢失数据。

Oracle：把提交的 sql 操作线写入了在线联机日志文件中，保持到了磁盘上，可以随时恢复。

**9、分区表和分区索引：**

MySQL：分区表还不太成熟稳定。

Oracle：分区表和分区索引功能很成熟，可以提高用户访问 db 的体验。

10、复制：

MySQL：复制服务器配置简单，但主库出问题时，丛库有可能丢失一定的数据。且需要手工切换丛库到主库。

Oracle：既有推或拉式的传统数据复制，也有 dataguard 的双机或多机容灾机制，主库出现问题是，可以自动切换备库到主库，但配置管理较复杂。

11、性能诊断：

MySQL：诊断调优方法较少，主要有慢查询日志。（现在的手段工具也挺多了）。

Oracle：有各种成熟的性能诊断调优工具，能实现很多自动分析、诊断功能 - 比如 awr、addm、sqltrace、tkproof 等。

12、权限与安全：

MySQL：用户与主机有关，感觉没有什么意义，另外更容易被仿冒主机及 ip 有可乘之机。

Oracle：权限与安全概念比较传统，中规中矩。

13、管理工具：

MySQL：管理工具较少，在 linux 下的管理工具的安装有时要安装额外的包（phpmyadmin， etc)，有一定复杂性。

Oracle：有多种成熟的命令行、图形界面、web 管理工具，还有很多第三方的管理工具，管理极其方便高效。

---

## 提高部分

---

###

---





---

参考链接：

[MySQL 与 Oracle 的区别](https://blog.csdn.net/baidu_37107022/article/details/77043959)
[]()

---
