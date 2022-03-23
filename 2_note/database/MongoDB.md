# MongoDB

---

## 基础部分

---

### MongoDB 索引

MongoDB 使用 B-树，所有节点都有 Data 域，只要找到指定索引就可以进行访问，

单次查询从结构上来看要快于 MySql。

最好不要建立重复索引；每个索引需要 8KB 的空间，同时 update、insert 操作会导致索引的调整，会稍微影响 write 的性能，索引只能使 read
操作收益，所以读写比高的应用可以考虑建立索引。

### BSON

mongodb 的 document 是 BSON 格式，松散的，原则上说任何一个 Colleciton 都可以保存任意结构的
document，甚至它们的格式千差万别，不过从应用角度考虑，包括业务数据分类和查询优化机制等，我们仍然建议每个 colleciton 中的 document 数据结构应该比较接近。

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

###

---

## 提高部分

---

###

---












---

参考链接：

- []()

---











