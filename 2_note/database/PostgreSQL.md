# PostgreSQL

---

## 基础部分

---

### MySQL 和 PostgreSQL 区别

**1、架构：**

MySQL：MySQL 分为两层，上层的 SQL 层和几个存储引擎（比如 InnoDB，MyISAM）。

PostgreSQL：只有一个存储引擎提供这两个功能。

**2、合法性验证：**

MySQL：一般会将数据合法性验证交给客户。

PostgreSQL：合法性难方面做得比较严格。

**3、对 SQL 标准的支持度：**

MySQL：只有部分支持，基本是只持了 SQL 92 标准的部分。

PostgreSQL：遵从 SQL 标准支持最好的数据库 - SQL2011 标准中，179 个核心特性（mandatory features ），做到至少 160 个，也支持了很多附加特性。

**4、ACID 的遵从性：**

MySQL：只有 InnoDB 等少量存储引擎遵从 ACID，所以客户选取就更加灵活。

PostgreSQL：完全遵从 ACID，确保满足所有 ACID 的需求支持的业务场景。

**5、索引的支持：**

MySQL：取决于存储引擎。MyISAM 是 BTREE 类型；InnoDB 是 B+TREE 类型。

PostgreSQL：支持 B- 树、哈希、R- 树和 Gist 索引 - 可以使用函数和条件索引，这使得 PostgreSQL 数据库的调优非常灵活。

**6、集群支持更好：**

MySQL：在数据量小的时候，数据库更趋于轻量化，MySQL 会更适合。

PostgreSQL：一旦数据量稍涨，计算量上升，PostgreSQL 会是更好的选择。

7、特性与速度：

MySQL：一开始焦点就在速度上，速度更快。

PostgreSQL：一开始焦点在特性和规范标准上，特性丰富。

8、出发点：

MySQL：是应用开发者创建出来的 DBMS。

PostgreSQL：由数据库开发者创建出来的 DBMS。

9、支持的业务场景：

MySQL：只支持 OLTP 场景，不可用于 OLAP 场景。

- MySQL5.7 以后才对 JSON 的有了支持。
- 简单和性能是设计第一目标，支持可靠性很高的业务存在一定的困难。
- 大数据库支持存在一定的困难，需要做单机多实例。

PostgreSQL：

- 支持 OLTP 场景也支持 OLAP 场景，混合性业务场景，类似 Oracle 数据库。
- 能很好的支持 JSON 文档型业务场景 金融级的可靠性，设计目标是以稳定性和可靠性为第一目标，支持大数据库，可以可靠支持几十 T 的大数据库。

**10、如何选型：**

MySQL：在数据量小的时候，数据库更趋于轻量化，MySQL 很适合。

PostgreSQL：一旦数据量稍涨，计算量上升，PostgreSQL 会是更好的选择。

### PostgreSQL 特性

1、外部数据源支持：

- 可以把 70 种外部数据源 (包括 MySQL, Oracle, CSV, hadoop …) 当成自己数据库中的表来查询。

**2、支持窗口函数：**

- 窗口函数提供跨行相关的当前查询行集执行计算的能力。
- 仅当调用跟着 OVER 子句的聚集函数，作为窗口函数；否则它们作为常规的聚合函数。
- 窗口也是一种分组，但和 group by 的分组不同。
- 窗口，可以提供分组之外，还可以执行对每个窗口进行计算。
- 可以想象成是 group by 后，然后对每个分组进行计算，而不像 Group by ，只是单纯地分组。
- OVER 子句能简单的解决 “每组取 top 5” 的这类问题。
- MySQL 不支持 OVER 子句，而 PostgreSQL 支持。
- MySQL 只有支持部分 SQL 标准。
- MySQL 不支持递归查询、通用表表达式 (Oracle 的 with 语句) 或者窗口函数 (分析函数)。

**3、支持地理信息处理扩展：**

- PostGIS 为 PostgreSQL 提供了存储空间地理数据的支持，使 PostgreSQL 成为了一个空间数据库，能够进行空间数据管理、数量测量与几何拓扑分析。
- O2O 业务场景中的 LBS 业务使用 PostgreSQL + PostGIS 有无法比拟的优势。

4、快速构建 REST API：

- PostgREST 可以方便的为任何 PostgreSQL 数据库提供完全的 RESTful API 服务。

5、没有字符串长度限制：

- 一般关系型数据库的字符串有限定长度 8k 左右，无限长 TEXT 类型的功能受限，只能作为外部大数据访问。
- 而 PostgreSQL 的 TEXT 类型可以直接访问，SQL 语法内置正则表达式，可以索引，还可以全文检索，或使用 xml xpath。
- MySQL 的各种 text 字段有不同的限制，要手动区分 small text, middle text, large text 等。
- PostgreSQL 没有这个限制，text 能支持各种大小

6、支持树状结构：

- 支持 R-trees 这样可扩展的索引类型，可以更方便地处理一些特殊数据。
- MySQL 处理树状的设计会很复杂，而且需要写很多代码，PostgreSQL 可以高效处理树结构。

7、支持图结构数据存储。

8、强悍的 SQL 编程能力：

- 支持递归，有非常丰富的统计函数和统计语法支持。

---

## 提高部分

---

###

---

---

参考链接：

- [MySQL 与 PostgreSQL 数据库功能对比](https://developer.aliyun.com/article/707482)
- [MySQL 与 PostgreSQL 比较，哪个更好、我们该选用哪个？](https://blog.csdn.net/weixin_36380516/article/details/113787668)
- [MySQL 与 PostgreSQL 对比](https://cloud.tencent.com/developer/article/1706949)
- []()

---




