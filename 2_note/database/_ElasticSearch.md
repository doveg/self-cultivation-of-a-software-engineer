# ElasticSearch / ES

---

## 基础部分

---

## ES 基本概念

1、index / 索引：

> ES 中的索引类似于传统数据库中的数据库，ES 中数据存储于索引中，索引是具有类似特性的文档的集合。

2、type / 类型：

> 类型类似于传统库中的表，类型是索引内部的逻辑分区，类型就是为那些拥有相同的域的文档做的预定义。

3、document / 文档：

> 文档是 Lucene 索引和搜索的原子单位，它是包含了一个或多个域的容器，基于 JSON 格式进行表示。

4、mapping / 映射：

> 所有的文档在存储之前都要首先进行分析。用户可根据需要定义如何将文本分割成 token，哪些 token 应该被过滤掉，以及哪些文本需要进行额外处理等等。

5、shard / 分片、replica / 副本：

> 将一个索引内部的数据分布地存储于多个节点，它通过将一个索引切分为多个底层物理的 Lucene 索引完成索引数据的分割存储功能，
>
> **这每一个物理的 Lucene 索引称为一个分片（shard）。**
>
> **每个分片其内部都是一个全功能且独立的索引，因此可由集群中的任何主机存储。**
>
> 创建索引时，用户可指定其分片的数量，默认数量为 5 个。
>
> Shard 有两种类型：primary 和 replica，即主 shard 及副本 shard。
>
> Primary shard 用于文档存储，每个新的索引会自动创建 5 个 Primary shard，当然此数量可在索引创建之前通过配置自行定义，
>
> 一旦创建完成，其 Primary shard 的数量将不可更改。
>
> Replica shard 是 Primary Shard 的副本，用于冗余数据及提高搜索性能。
>
> 每个 Primary shard 默认配置了一个 Replica shard，但也可以配置多个，且其数量可动态更改。
>
> ES 会根据需要自动增加或减少这些 Replica shard 的数量。
>
> ES 集群可由多个节点组成，各 Shard 分布式地存储于这些节点上。
>
> ES 可自动在节点间按需要移动 shard，例如增加节点或节点故障时。简而言之，分片实现了集群的分布式存储，而副本实现了其分布式处理及冗余功能。

### 倒排索引

~~索引是构成搜索引擎的核心技术之一，索引在日常生活中其实也是非常常见的，比如当我们看一本书的时候，我们首先会看书的目录，通过目录可以快速定位到某一章节的页码，加快对内容的查询速度。~~

倒排索引 / 反向索引：

**用来存储在全文搜索下某个单词在一个文档或者一组文档中的存储位置的映射，它是文档检索系统中最常用的数据结构。**

Lucene 的倒排索引，增加了最左边的一层「字典树」term index，

它不存储所有的单词，只存储单词前缀，通过字典树找到单词所在的块，也就是单词的大概位置，再在块里二分查找，找到对应的单词，再找到单词对应的文档列表。

Lucene 的实现会要更加复杂，针对不同的数据结构采用不同的字典索引，使用了 FST 模型、BKDTree 等结构。

真实的倒排记录也并非一个链表，而是采用了 SkipList \ 跳表、BitSet \ 位图 等结构。

### 游标查询

scroll / 游标查询：可以用来对 Elasticsearch 有效地执行大批量的文档查询，而又不用付出深度分页那种代价。

游标查询允许我们先做查询初始化，然后再批量地拉取结果。这有点儿像传统数据库中的 cursor 。

游标查询会取某个时间点的快照数据。查询初始化之后索引上的任何变化会被它忽略。它通过保存旧的数据文件来实现这个特性，结果就像保留初始化时的索引视图一样。

深度分页的代价根源是结果集全局排序，如果去掉全局排序的特性的话查询结果的成本就会很低。游标查询用字段 _doc 来排序。

这个指令让 Elasticsearch 仅仅从还有结果的分片返回下一批结果。

启用游标查询可以通过在查询的时候设置参数 scroll 的值为我们期望的游标查询的过期时间。
游标查询的过期时间会在每次做查询的时候刷新，所以这个时间只需要足够处理当前批的结果就可以了，而不是处理查询结果的所有文档的所需时间。
这个过期时间的参数很重要，因为保持这个游标查询窗口需要消耗资源，所以我们期望如果不再需要维护这种资源就该早点儿释放掉。设置这个超时能够让 Elasticsearch
在稍后空闲的时候自动释放这部分资源。

### 聚合索引的返回值

返回的是聚合计算和分析的结果。

ES 中 group by 称为分桶，桶聚合（bucketing）。

不支持对聚合结果进行二次聚合。

### 搜索的底层原理

倒排索引

###

###

---

## 提高部分

---

### Lucene

segment/doc/term/token 都是 Lucene 中的概念。

**document：文档，索引和搜索的主要数据载体，对应写入到 ES 中的一个 doc。**

**field: 字段，document 中的各个字段。**

**term: 词项，搜索时的一个单位，代表文本中的某个词。**

**token: 词条，词项（term）在字段（field）中的一次出现，包括词项的文本、开始和结束的位移、类型等信息。**

Lucene 内部使用的是倒排索引的数据结构， 将词项（term）映射到文档（document）。

**segment：段，每个 shard 分片是一个 Lucene 实例，每个分片由多个 segment 组成。每个 segment 占用内存，文件句柄等。**

### ES 副本写数据的过程

> 客户端随机选择一个 node 发送请求过去，这个 node 就是 coordinating node（协调节点）。
>
> coordinating node，对 document 进行路由，将请求转发给对应的 node（有 primary shard）。
>
> 实际的 node 上的 primary shard 处理请求，然后将数据同步到 replica node。
>
> coordinating node，如果发现 primary node 和所有 replica node 都搞定之后，就返回响应结果给客户端。

### ES 主分片写数据的过程

在写 primary 的过程中同时还要持久到本地。

1）先写入 buffer，在 buffer 里的时候数据是搜索不到的；同时将数据写入 translog 日志文件

2）如果 buffer 快满了，或者到一定时间，就会将 buffer 数据 refresh 到一个新的 segment file 中，但是此时数据不是直接进入 segment file
的磁盘文件的，而是先进入 os cache 的。这个过程就是 refresh。

每隔 1 秒钟，ES 将 buffer 中的数据写入一个新的 segment file，每秒钟会产生一个新的磁盘文件，segment file，这个 segment file 中就存储最近 1 秒内
buffer 中写入的数据。

但是如果 buffer 里面此时没有数据，那当然不会执行 refresh 操作咯，每秒创建换一个空的 segment file，如果 buffer 里面有数据，默认 1 秒钟执行一次 refresh
操作，刷入一个新的 segment file 中。

操作系统里面，磁盘文件其实都有一个东西，叫做 os cache，操作系统缓存，就是说数据写入磁盘文件之前，会先进入 os cache，先进入操作系统级别的一个内存缓存中去。

只要 buffer 中的数据被 refresh 操作，刷入 os cache 中，就代表这个数据就可以被搜索到了。

为什么叫 ES 是准实时的？NRT，near real-time，准实时。默认是每隔 1 秒 refresh 一次的，所以 ES 是准实时的，因为写入的数据 1 秒之后才能被看到。

可以通过 ES 的 restful api 或者 java api，手动执行一次 refresh 操作，就是手动将 buffer 中的数据刷入 os cache 中，让数据立马就可以被搜索到。

只要数据被输入 os cache 中，buffer 就会被清空了，因为不需要保留 buffer 了，数据在 translog 里面已经持久化到磁盘去一份了。

3）只要数据进入 os cache，此时就可以让这个 segment file 的数据对外提供搜索了。

4）重复 1~3 步骤，新的数据不断进入 buffer 和 translog，不断将 buffer 数据写入一个又一个新的 segment file 中去，每次 refresh 完 buffer
清空，translog 保留。随着这个过程推进，translog 会变得越来越大。当 translog 达到一定长度的时候，就会触发 commit 操作。

buffer 中的数据，倒是好，每隔 1 秒就被刷到 os cache 中去，然后这个 buffer 就被清空了。所以说这个 buffer 的数据始终是可以保持住不会填满 ES 进程的内存的。

每次一条数据写入 buffer，同时会写入一条日志到 translog 日志文件中去，所以这个 translog 日志文件是不断变大的，当 translog 日志文件大到一定程度的时候，就会执行
commit 操作。

5）commit 操作发生第一步，就是将 buffer 中现有数据 refresh 到 os cache 中去，清空 buffer。

6）将一个 commit point 写入磁盘文件，里面标识着这个 commit point 对应的所有 segment file。

7）强行将 os cache 中目前所有的数据都 fsync 到磁盘文件中去。

translog 日志文件的作用是什么？就是在你执行 commit 操作之前，数据要么是停留在 buffer 中，要么是停留在 os cache 中，无论是 buffer 还是 os cache
都是内存，一旦这台机器死了，内存中的数据就全丢了。

所以需要将数据对应的操作写入一个专门的日志文件，translog 日志文件中，一旦此时机器宕机，再次重启的时候，ES 会自动读取 translog 日志文件中的数据，恢复到内存 buffer 和 os
cache 中去。

commit 操作：1、写 commit point；2、将 os cache 数据 fsync 强刷到磁盘上去；3、清空 translog 日志文件。

8）将现有的 translog 清空，然后再次重启启用一个 translog，此时 commit 操作完成。默认每隔 30 分钟会自动执行一次 commit，但是如果 translog 过大，也会触发
commit。整个 commit 的过程，叫做 flush 操作。我们可以手动执行 flush 操作，就是将所有 os cache 数据刷到磁盘文件中去。

不叫做 commit 操作，flush 操作。ES 中的 flush 操作，就对应着 commit 的全过程。我们也可以通过 ES api，手动执行 flush 操作，手动将 os cache
中的数据 fsync 强刷到磁盘上去，记录一个 commit point，清空 translog 日志文件。

9）translog 其实也是先写入 os cache 的，默认每隔 5 秒刷一次到磁盘中去，所以默认情况下，可能有 5 秒的数据会仅仅停留在 buffer 或者 translog 文件的 os
cache 中，如果此时机器挂了，会丢失 5 秒钟的数据。但是这样性能比较好，最多丢 5 秒的数据。也可以将 translog 设置成每次写操作必须是直接 fsync 到磁盘，但是性能会差很多。

实际上你在这里，如果面试官没有问你 ES 丢数据的问题，你可以在这里给面试官炫一把，你说，其实 ES 第一是准实时的，数据写入 1 秒后可以搜索到；可能会丢失数据的，你的数据有 5 秒的数据，停留在
buffer、translog os cache、segment file os cache 中，有 5 秒的数据不在磁盘上，此时如果宕机，会导致 5 秒的数据丢失。

如果你希望一定不能丢失数据的话，你可以设置个参数，官方文档，百度一下。每次写入一条数据，都是写入 buffer，同时写入磁盘上的
translog，但是这会导致写性能、写入吞吐量会下降一个数量级。本来一秒钟可以写 2000 条，现在你一秒钟只能写 200 条，都有可能。

10）如果是删除操作，commit 的时候会生成一个.del 文件，里面将某个 doc 标识为 deleted 状态，那么搜索的时候根据.del 文件就知道这个 doc 被删除了。

11）如果是更新操作，就是将原来的 doc 标识为 deleted 状态，然后新写入一条数据。

12）buffer 每次 refresh 一次，就会产生一个 segment file，所以默认情况下是 1 秒钟一个 segment file，segment file 会越来越多，此时会定期执行
merge。

13）每次 merge 的时候，会将多个 segment file 合并成一个，同时这里会将标识为 deleted 的 doc 给物理删除掉，然后将新的 segment file
写入磁盘，这里会写一个 commit point，标识所有新的 segment file，然后打开 segment file 供搜索使用，同时删除旧的 segment file。

ES 里的写流程，有 4 个底层的核心概念，refresh、flush、translog、merge。

当 segment file 多到一定程度的时候，ES 就会自动触发 merge 操作，将多个 segment file 给 merge 成一个 segment file。

### ES 读数据的过程

查询，GET 某一条数据，写入了某个 document，这个 document 会自动给你分配一个全局唯一的 id，doc id，

同时也是根据 doc id 进行 hash 路由到对应的 primary shard 上面去。也可以手动指定 doc id，比如用订单 id，用户 id。

你可以通过 doc id 来查询，会根据 doc id 进行 hash，判断出来当时把 doc id 分配到了哪个 shard 上面去，从那个 shard 去查询。

> 客户端发送请求到任意一个 node，成为 coordinate node。
>
> coordinate node 对 document 进行路由，将请求转发到对应的 node，
>
> 此时会使用 round-robin 随机轮询算法，在 primary shard 以及其所有 replica 中随机选择一个，让读请求负载均衡。
>
> 接收请求的 node 返回 document 给 coordinate node。
>
> coordinate node 返回 document 给客户端。

### ES 搜索数据的过程

> 客户端发送请求到一个 coordinate node。
>
> 协调节点将搜索请求转发到所有的 shard 对应的 primary shard 或 replica shard 也可以。
>
> query phase：每个 shard 将自己的搜索结果（其实就是一些 doc id），返回给协调节点，由协调节点进行数据的合并、排序、分页等操作，产出最终结果。
>
> fetch phase：接着由协调节点，根据 doc id 去各个节点上拉取实际的 document 数据，最终返回给客户端。

###

###

---











---

参考链接：

- [ElasticSearch 原理](https://www.jianshu.com/p/3b68f351bdc7)
- [理解 ElasticSearch 工作原理](https://www.jianshu.com/p/52b92f1a9c47)
- [Elasticsearch: 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/scroll.html)
- []()

---



















