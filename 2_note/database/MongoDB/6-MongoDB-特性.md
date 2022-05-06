# MongoDB

---

## 基础部分

---

### 存储引擎 Wired Tiger

存储引擎的核心工作是管理数据如何在磁盘和内存上读写，从 MongoDB 3.2 开始支持多种存储引擎：Wired Tiger，MMAPv1 和 In-Memory，

其中默认为 **Wired Tiger**。

重要数据结构和 Page：

**B+ Tree：**

存储引擎最核心的功能就是完成数据在客户端 - 内存 - 磁盘之间的交互。

客户端是不可控的，因此如何设计一个高效的数据结构和算法，实现数据快速在内存和磁盘间交互就是存储引擎需要考虑的核心问题。

目前大多少流行的存储引擎都是基于 B/B+ Tree 和 LSM (Log Structured Merge) Tree 来实现。

Oracle、SQL Server、DB2、MySQL (InnoDB) 这些传统的关系数据库依赖的底层存储引擎是基于 B+ Tree 开发的；

而像 Cassandra、Elasticsearch (Lucene)、Google Bigtable、Apache HBase、LevelDB 和 RocksDB，这些当前比较流行的 NoSQL
数据库，存储引擎是基于 LSM 开发的。

MongoDB 虽然是 NoSQL 的，但是其存储引擎 Wired Tiger 却是用的 B+ Tree，因此有种说法是 MongoDB 是最接近 SQL 的 NoSQL 存储引擎。

好了，我们这里知道 Wired Tiger 的存储结构是 B+ Tree 就行了，至于什么是 B+ Tree，它有些啥优势网都有很多文章，这里就不在赘述了。

**Page：**

Wired Tiger 在内存和磁盘上的数据结构都 B+ Tree，B+ 的特点是中间节点只有索引，数据都是存在叶节点。Wired Tiger 管理数据结构的基本单元 Page。

Page 上有 3 个重要的 list WT_ROW、WT_UPDATE、WT_INSERT。这个 Page 的组织结构和 Page 的 3 个 list 对后面理解 cache、checkpoint
等操作很重要：

    内存中的 Page 树是一个 checkpoint
    叶节点 Page 的 WT_ROW：是从磁盘加载进来的数据数组
    叶节点 Page 的 WT_UPDATE：是记录数据加载之后到下个 checkpoint 之间被修改的数据
    叶节点 Page 的 WT_INSERT：是记录数据加载之后到下个 checkpoint 之间新增的数据

### 数据压缩

MongoDB 的另外一个比较重要的特性是数据压缩，MongoDB 会自动把客户数据压缩之后再落盘，这样就可以节省存储空间。

MongoDB 的数据压缩算法有多种：

    Snappy：默认的压缩算法，压缩比 3 ～ 5 倍
    Zlib：高度压缩算法，压缩比 5 ～ 7 倍
    前缀压缩：索引用的压缩算法，简单理解就是丢掉重复的前缀
    zstd：MongoDB 4.2 之后新增的压缩算法，拥有更好的压缩率

现在推荐的 MongoDB 版本是 4.0，在这个版本下推荐使用 snappy 算法，虽然 zlib 有更高的压缩比，但是读写会有一定的性能波动，不适合核心业务，但是比较适合流水、日志等场景。




---

