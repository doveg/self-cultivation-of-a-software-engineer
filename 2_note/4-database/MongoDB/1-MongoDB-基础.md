# MongoDB

---

## 基础部分

---

### MongoDB 的核心特性

No Schema、高可用、分布式（可平行扩展），另外 MongoDB 自带数据压缩功能，使得同样的数据存储所需的资源更少。

### MongoDB 的 No Schema

No Schema 特性带来的好处包括：

    强大的表现能力：对象嵌套和数组结构可以让数据库中的对象具备更高的表现能力，能够用更少的数据对象表现复杂的领域模型对象。
    便于开发和快速迭代：灵活的字段管理，使得项目迭代新增字段非常容易
    降低运维成本：数据对象结构变更不需要执行 DDL 语句，降低 Online 环境的数据库操作风险，特别是在海量数据分库分表场景。
    MongoDB 在提供 No Schema 特性基础上，提供了部分可选的 Schema 特性：Validation。其主要功能有包括：
    规定某个 Document 对象必须包含某些字段
    规定 Document 某个字段的数据类型 $type（中 $ 开头的都是关键字）
    规定 Document 某个字段的取值范围：可以是枚举 $in，或者正则 $$regex

上面的字段包含内嵌文档的，也就是说，你可以指定 Document 内任意一层 JSON 文件的字段属性。

validator 的值有两种，一种是简单的 JSON Object，另一种是通过关键字 $jsonSchema 指定。

### BSON

mongodb 的 document 是 BSON 格式，松散的，原则上说任何一个 Collection 都可以保存任意结构的 document，甚至它们的格式千差万别，

不过从应用角度考虑，包括业务数据分类和查询优化机制等，我们仍然建议每个 Collection 中的 document 数据结构应该比较接近。

### MongoDB 的 explain

explain 是 MongoDB 的查询计划工具，和 MySql 的 explain 功能相同，都是用来分析一条语句的索引使用情况、影响行数、执行时间等。

explain 有三种参数分别对应结果输出的三部分数据：

> queryPlanner：MongoDB 运行查询优化器对当前的查询进行评估并选择一个最佳的查询计划。
>
> executionStats：mongoDB 运行查询优化器对当前的查询进行评估并选择一个最佳的查询计划进行执行。在执行完毕后返回这个最佳执行计划执行完成时的相关统计信息。
>
> allPlansExecution：即按照最佳的执行计划执行以及列出统计信息，如果有多个查询计划，还会列出这些非最佳执行计划部分的统计信息。

explain 是一个非常有用的工具，建议在一个数据量较大的数据库上开发新功能时，一定要用 explain 分析一下自己的语句是否合理、索引是否合理，避免在项目上线之后出现问题。

### MongoDB Shell 和 SQL

| # | SQL 操作 | MongoDB 操作 |
|:---:|:---|:---|
| 01 | where | $match |
| 02 | group by | $group |
| 03 | having | $match |
| 04 | select | $project |
| 05 | order by | $sort |
| 06 | limit　 | $limit |
| 07 | sum() | $sum |
| 08 | count() | $sum |
| 09 | join | $lookup |
|<img width=50px/>|<img width=200px/>|<img width=200px/>|<img width=100px/>|

### MongoDB 的分组查询

aggregate

    $group 操作

### MongoDB 的多表联合查询

aggregate

    $lookup 操作

---

参考链接：

- [MongoDB 全方位知识图谱](https://mp.weixin.qq.com/s/qStIOFcynQCiYw-WppAebg)
- []()
- []()
- []()

---





