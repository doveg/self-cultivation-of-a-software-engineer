## Neo4j

---

#### 基础部分

###### 索引

Neo4j 的索引实际上是通过 Lucene 实现的

###### 事务

Neo4j 完整支持事务，满足 ACID 性质

1. 原子性（Atomicity）
2. 一致性（Consistency）
3. 隔离性（Isolation）
4. 持久性（Durability）

###### 图算法

1. 中心性算法（Centrality algorithms）
2. 社区发现算法（Community detection algorithms）
3. 路径寻找算法（Path Finding algorithms）
4. 相似度算法（Similarity algorithms）
5. 链接预测算法（Link Prediction algorithms）
6. 链接预测算法（Link Prediction algorithms）

###### Neo4j 的优点

1. 直观：数据的插入，查询操作很直观
2. 方便：提供的图搜索和图遍历方法很方便，速度也是比较快的
3. 更快：在数据量较大的情况下更快；比如在 MySQL 中存储大量关系数据需要很多表，并且表之间联系较多，这时候 MySQL 的性能会成为瓶颈

###### Neo4j 的缺点

1. 对大数据量不友好：当数据过大时插入速度可能会越来越慢
2. 超大节点的性能问题：当有一个节点的边非常多时，有关这个节点的操作的速度将大大下降
3. 数据库内存占用问题：需要计算后为其”预留“内存

###### Neo4j 应用场景

适用于：存储”修改较少，查询较多，没有超大节点“的图数据

不适用于：

1. 记录大量基于事件的数据（例如日志条目或传感器数据）
2. 对大规模分布式数据进行处理，类似于 Hadoop
3. 二进制数据存储
4. 适合于保存在关系型数据库中的结构化数据

---

#### 提高部分

######

---




---

参考链接：

- [Neo4j 算法介绍](https://www.jianshu.com/p/5254368f059b)

---
















