# MongoDB

---

## 基础部分

---

### BSON

mongodb 的 document 是 BSON 格式，松散的，原则上说任何一个 Colleciton 都可以保存任意结构的 document，甚至它们的格式千差万别，

不过从应用角度考虑，包括业务数据分类和查询优化机制等，我们仍然建议每个 colleciton 中的 document 数据结构应该比较接近。

### MongoDB 架构模式

Replica set：

复制集，mongodb 的架构方式之一 ，通常是三个对等的节点构成一个 “复制集” 集群，有 “primary” 和 secondary 等多中角色（稍后详细介绍），其中 primary
负责读写请求，secondary 可以负责读请求，这有配置决定，其中 secondary 紧跟 primary 并应用 write 操作；如果 primay 失效，则集群进行 “多数派”
选举，选举出新的 primary，即 failover 机制，即 HA 架构。

复制集解决了单点故障问题，也是 mongodb 垂直扩展的最小部署单位，当然 sharding cluster 中每个 shard 节点也可以使用 Replica set 提高数据可用性。

Sharding cluster：

分片集群，数据水平扩展的手段之一；replica set 这种架构的缺点就是 “集群数据容量” 受限于单个节点的磁盘大小，如果数据量不断增加，对它进行扩容将时非常苦难的事情，所以我们需要采用
Sharding 模式来解决这个问题。

将整个 collection 的数据将根据 sharding key 被 sharding 到多个 mongod 节点上，即每个节点持有 collection
的一部分数据，这个集群持有全部数据，原则上 sharding 可以支撑数 TB 的数据。

### MongoDB 索引类型

MongoDB 的索引和 MySql 的索引有点不一样，它的索引在创建时必须指定顺序（1：升序，-1：降序），同时所有的集合都有一个默认索引 _id，这是一个唯一索引，类似 MySql 的主键。

MongoDB 支持的索引类型有：

    单字段索引：建立在单个字段上的索引，索引创建的排序顺序无所谓，MongoDB 可以头 / 尾开始遍历。
    复合索引：建立在多个字段上的索引。
    多 key 索引：我们知道 MongoDB 的一个字段可能是数组，在对这种字段创建索引时，就是多 key 索引。MongoDB 会为数组的每个值创建索引。就是说你可以按照数组里面的值做条件来查询，这个时候依然会走索引。
    Hash 索引：按数据的哈希值索引，用在 hash 分片集群上。
    地理位置索引：基于经纬度的索引，适合 2D 和 3D 的位置查询。
    文本索引：MongoDB 虽然支持全文索引，但是性能低下，暂时不建议使用。

### MongoDB 索引注意事项

索引功能强大，但是也有很多限制，使用索引时一定要注意一些问题。

复合索引：

复合索引有几个问题需要注意：

    复合索引遵循前缀匹配原则：{userid:1,score:-1} 的索引隐含了 {userid:1} 的索引
    避免内存排序：复合索引除第一个字段之外，其他字段的查询排序方式，必须和索引排序方式一致，否则会导致内存排序。如前面的索引，可以支持 {userid:-1,score:-1} 的查询，同时也能支持 {userid:1,score:1} 的查询，只是后一种需要内存排序 score 字段。
    索引交集：索引交集时查询优化器的优化方案，很少用到，尽量不要依赖这个功能。索引交集本质上就有创建两个独立的单字段索引，在查询保护两个字段时，优化器自动做索引交集。如 {user:1} + {score:-1} 两个索引的交集可以支持前面的 {userid:1,score:1} 的查询

后台创建索引：

在对一个已经拥有较大数据集的 Collection 创建索引时，建议通过创建命令参数指定后台创建，不会阻塞命令和意外中断。

但是，在后台创建多个索引时，不能命令执行完就接着下一个。因为是后台创建，命令行虽然推出了，但是索引还没创建完。

这个时候如果同事输入多个创建索引命令，会因为大量的写操作和数据复制导致系统 cpu 耗尽。这个时候需要观察系统监控，确定第一个索引创建完了再执行下一个。

### MongoDB 的 explain

explain 是 MongoDB 的查询计划工具，和 MySql 的 explain 功能相同，都是用来分析一条语句的索引使用情况、影响行数、执行时间等。

explain 有三种参数分别对应结果输出的三部分数据：

    queryPlanner：MongoDB 运行查询优化器对当前的查询进行评估并选择一个最佳的查询计划。
    exectionStats：mongoDB 运行查询优化器对当前的查询进行评估并选择一个最佳的查询计划进行执行。在执行完毕后返回这个最佳执行计划执行完成时的相关统计信息。
    allPlansExecution：即按照最佳的执行计划执行以及列出统计信息，如果有多个查询计划，还会列出这些非最佳执行计划部分的统计信息。

explain 是一个非常有用的工具，建议在一个数据量较大的数据库上开发新功能时，一定要用 explain 分析一下自己的语句是否合理、索引是否合理，避免在项目上线之后出现问题。

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

上面的字段包含内嵌文档的，也就是说，你可以指定 Document 内任意一层 JSON 文件的字段属性。validator 的值有两种，一种是简单的 JSON Object，另一种是通过关键字
$jsonSchema 指定。

### MongoDB 的高可用

### MongDB 复制集群

MongoDB 高可用的基础是复制集群，复制集群本质来说就是一份数据存多份，保证一台机器挂掉了数据不会丢失。一个副本集至少有 3 个节点组成：

1、至少一个主节点（**Primary**）：负责整个集群的写操作入口，主节点挂掉之后会自动选出新的主节点。

2、一个或多个从节点（**Secondary**）：一般是 2 个或以上，从主节点同步数据，在主节点挂掉之后选举新节点。

3、零个或 1 个仲裁节点（**Arbiter**）：这个是为了节约资源或者多机房容灾用，只负责主节点选举时投票不存数据，保证能有节点获得多数赞成票。

从上面的节点类型可以看出，一个三节点的复制集群可能是 PSS 或者 PSA 结构。

PSA 结构优点是节约成本，但是缺点是 Primary 挂掉之后，一些依赖 majority（多数）特性的写功能出问题，因此一般不建议使用。

### 读写策略

Write Concern —— 写策略

控制服务端一次写操作在什么情况下才返回客户端成功，由两个参数控制：

    w 参数：控制数据同步到多少个节点才算成功，取值范围 0～节点个数 /majority。0 表示服务端收到请求就返回成功，major 表示同步到大多数（大于等于 N/2）节点才返回成功。其它值表示具体的同步节点个数。默认为 1，表示 Primary 写成功就返回成功。
    j 参数：控制单个节点是否完成 oplog 持久化到磁盘才返回成功，取值范围 true/false。默认 false，因此可能最多丢 100ms 数据。

Read Concern —— 读策略

控制客户端从什么节点读取数据，默认为 primary，具体参数及含义：

    primary：读主节点
    primaryPreferred：优先读主节点，不存在时读从节点
    secondary：读从节点
    secondaryPreferred：优先读从节点，不存在时读主节点
    nearest：就近读，不区分主节点还是从节点，只考虑节点延时。

Read Concern Level —— 读级别

这是一个非常有意思的参数，也是最不容易理解的异常参数。它主要控制的是读到的数据是不是最新的、是不是持久的，最新的和持久的是一对矛盾，最新的数据可能会被回滚，持久的数据可能不是最新的，这需要业务根据自己场景的容忍度做决策，前提是你的先知道有哪些，他们代表什么意义：

    local：直接从查询节点返回，不关心这些数据被同步到了多少个节点。存在被回滚的风险。
    available：适用于分片集群，和 local 差不多，也存在被回滚的风险。
    majority：返回被大多数节点确认过的数据，不会被回滚，前提是 WriteConcern=majority
    linearizable：适用于事务，读操作会等待在它开始前已经在执行的事务提交了才返回
    snapshot：适用于事务，快照隔离，直接从快照去。

### MongoDB 的可扩展性 —— 分片集群

分片集群架构

MongoDB 的分片集群由如下三个部分组成：

    Config：配置，本质上是一个 MongoDB 的副本集，负责存储集群的各种元数据和配置，如分片地址、chunks 等
    Mongos：路由服务，不存具体数据，从 Config 获取集群配置讲请求转发到特定的分片，并且整合分片结果返回给客户端。
    Mongod：一般将具体的单个分片叫 mongod，实质上每个分片都是一个单独的复制集群，具备负责集群的高可用特性。

分片算法

MongoDB 支持两种分片算法来满足不同的查询需求：

    区间分片：可以按 shardkey 做区间查询的分片算法，直接按照 shardkey 的值来分片。
    hash 分片：用的最多的分片算法，按 shardkey 的 hash 值来分片。hash 分片可以看作一种特殊的区间分片。

### 数据压缩

MongoDB 的另外一个比较重要的特性是数据压缩，MongoDB 会自动把客户数据压缩之后再落盘，这样就可以节省存储空间。MongoDB 的数据压缩算法有多种：

    Snappy：默认的压缩算法，压缩比 3 ～ 5 倍
    Zlib：高度压缩算法，压缩比 5 ～ 7 倍
    前缀压缩：索引用的压缩算法，简单理解就是丢掉重复的前缀
    zstd：MongoDB 4.2 之后新增的压缩算法，拥有更好的压缩率

现在推荐的 MongoDB 版本是 4.0，在这个版本下推荐使用 snappy 算法，虽然 zlib 有更高的压缩比，但是读写会有一定的性能波动，不适合核心业务，但是比较适合流水、日志等场景。

###

---

## 提高部分

---

### 存储引擎 Wired Tiger

存储引擎的核心工作是管理数据如何在磁盘和内存上读写，从 MongoDB 3.2 开始支持多种存储引擎：Wired Tiger，MMAPv1 和 In-Memory，其中默认为 Wired Tiger。

重要数据结构和 Page

B+ Tree：

存储引擎最核心的功能就是完成数据在客户端 - 内存 - 磁盘之间的交互。

客户端是不可控的，因此如何设计一个高效的数据结构和算法，实现数据快速在内存和磁盘间交互就是存储引擎需要考虑的核心问题。

目前大多少流行的存储引擎都是基于 B/B+ Tree 和 LSM (Log Structured Merge) Tree 来实现。

Oracle、SQL Server、DB2、MySQL (InnoDB) 这些传统的关系数据库依赖的底层存储引擎是基于 B+ Tree 开发的；

而像 Cassandra、Elasticsearch (Lucene)、Google Bigtable、Apache HBase、LevelDB 和 RocksDB，这些当前比较流行的 NoSQL
数据库，存储引擎是基于 LSM 开发的。

MongoDB 虽然是 NoSQL 的，但是其存储引擎 Wired Tiger 却是用的 B+ Tree，因此有种说法是 MongoDB 是最接近 SQL 的 NoSQL 存储引擎。

好了，我们这里知道 Wired Tiger 的存储结构是 B+ Tree 就行了，至于什么是 B+ Tree，它有些啥优势网都有很多文章，这里就不在赘述了。

Page：

Wired Tiger 在内存和磁盘上的数据结构都 B+ Tree，B+ 的特点是中间节点只有索引，数据都是存在叶节点。Wired Tiger 管理数据结构的基本单元 Page。

Page 上有 3 个重要的 list WT_ROW、WT_UPDATE、WT_INSERT。这个 Page 的组织结构和 Page 的 3 个 list 对后面理解 cache、checkpoint
等操作很重要：

    内存中的 Page 树是一个 checkpoint
    叶节点 Page 的 WT_ROW：是从磁盘加载进来的数据数组
    叶节点 Page 的 WT_UPDATE：是记录数据加载之后到下个 checkpoint 之间被修改的数据
    叶节点 Page 的 WT_INSERT：是记录数据加载之后到下个 checkpoint 之间新增的数据

###

###

###

---












---

参考链接：

- [MongoDB 全方位知识图谱](https://mp.weixin.qq.com/s/qStIOFcynQCiYw-WppAebg)
- []()
- []()
- []()

---











