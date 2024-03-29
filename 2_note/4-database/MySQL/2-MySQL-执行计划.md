# MySQL

---

## 实践

---

### 执行计划

视频讲解：[SQL 面试题：索引和性能优化](https://www.bilibili.com/video/BV14L4y1M7c6)

视频讲解：[看不懂 MySQL 执行计划谈什么 SQL 优化，几个实例带你整明白 (1)](https://www.bilibili.com/video/BV1zL4y1p7dM)

SQL 在数据库中执行时的表现情况，通常用于 SQL 性能分析，优化等场景。预先估计查询究竟要涉及多少行、使用哪些索引、运行时间。

> 存储引擎在执行 sql 的时候，把一条 sql 分解，列出来每一步需要干什么，并按照步骤依次执行，这样我们就能看出来哪个步骤耽误了时间

| # | 名称 | 简介 | 值 |
|:---:|:---|:---|:---:|
| 01 | id | select 查询的序列号，包含一组数字，表示查询中执行 select 子句或操作表的顺序 |  |
| 02 | **select_type** | 查询类型 | simple、primary、union |
| 03 | table | 正在访问哪个表 |  |
| 04 | partitions | 匹配的分区 |  |
| 05 | **type** | 访问的类型 | All、index、range、ref、eq_ref、const、system、Null  |
| 06 | possible_keys | 显示可能应用在这张表中的索引，一个或多个，但不一定实际使用到 |  |
| 07 | **key** | 实际使用到的索引，如果为 NULL，则没有使用索引 |  |
| 08 | key_len | 表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度 |  |
| 09 | ref | 显示索引的哪一列被使用了，如果可能的话，是一个常数，哪些列或常量被用于查找索引列上的值 |  |
| 10 | **rows** | 根据表统计信息及索引选用情况，大致估算出找到所需的记录所需读取的行数 |  |
| 11 | filtered | 查询的表行占表的百分比 |  |
| 12 | **Extra** | 包含不适合在其它列中显示但十分重要的额外信息 |  |
|<img width=50px/>|<img width=150px/>|<img width=300px/>|<img width=200px/>|

1、id

> 表示查询中 select 操作表的顺序，按顺序从大到依次执行（不是表中的自增主键）
>
> id 值相同执行顺序从上到下。 id 值不同时 id 值大的先执行。

2、select_type

> 这一列显示了对应行是简单还是复杂。
>
> SIMPLE 值意味着查询不包括子查询和 UNION。
>
> 查询有任何复杂的子部分，则最外层标记为 PRIMARY

**3、type**

> 该属性表示访问类型，有很多种访问类型。
>
> 最常见的其中包括以下几种:
>
> ALL (全表扫描)，index (索引扫描)，range (范围扫描)，ref (非唯一索引扫描)，eq_ref (唯一索引扫描，)，(const) 常数引用，访问速度依次由慢到快。
>
> 提示：慢 SQL 是否走索引，走了什么索引，也就可以通过该属性查看了。

4、table

> 输出数据行所在的表的名称

5、possible_keys

> 指出 MySQL 能使用哪些索引来优化查询，查询所涉及的列上的索引都会被列出，但不一定会被使用，算是个提示作用。

**6、key**

> 显示 MySQL 实际使用的索引，其中就包括主键索引 (PRIMARY)，或者自建索引的名字。
>
> 如果没有可用的索引，则显示为 NULL。

7、key_len

> 表示索引字段的最大可能长度，KEY_LEN 的长度由字段定义计算而来，并非数据的实际长度，
>
> 当 key 字段的值为 null 时，索引的长度就是 null。注意，key_len 的值可以告诉你在联合索引中 MySQL 会真正使用了哪些索引。

**8、ref**

> 表示哪些列或常量被用于查找索引列上的值。
>
> 连接匹配条件。如果走主键索引的话，该值为: const；全表扫描的话，为 null 值。

**9、rows**

> 扫描行数，也就是说，需要扫描多少行，才能获取目标行数，一般情况下会大于返回行数。
>
> 通常情况下，rows 越小，效率越高，也就有大部分 SQL 优化，都是在减少这个值的大小。
>
> 注意：理想情况下扫描的行数与实际返回行数理论上是一致的，但这种情况及其少，如关联查询，扫描的行数就会比返回行数大大增加)

**10、Extra**

> 这个属性非常重要，该属性中包括执行 SQL 时的真实情况信息，如上面所属，使用到的是”using where”，表示使用 where 筛选得到的值。
>
> 常用的有：“Using temporary”: 使用临时表 “using filesort”: 使用文件排序

---

## 实践部分

---

### processlist

show processlist 或 show full processlist 可以查看当前 MySQL 是否有压力，正在运行的 SQL，有没有慢 SQL 正在执行。

返回参数如下：

> id：线程 ID，可以用 kill id 杀死某个线程。
>
> db：数据库名称。
>
> user：数据库用户。
>
> host：数据库实例的 IP。
>
> command：当前执行的命令，比如 Sleep，Query，Connect 等。
>
> time：消耗时间，单位秒。
>
> state：执行状态，主要有以下状态：
>
> > Sleep，线程正在等待客户端发送新的请求。
> >
> > Locked，线程正在等待锁。
> >
> > Sending data，正在处理 SELECT 查询的记录，同时把结果发送给客户端。
> >
> > Kill，正在执行 kill 语句，杀死指定线程。
> >
> > Connect，一个从节点连上了主节点。
> >
> > Quit，线程正在退出。
> >
> > Sorting for group，正在为 GROUP BY 做排序。
> >
> > Sorting for order，正在为 ORDER BY 做排序。
>
> info：正在执行的 SQL 语句。




---

## 参考链接

---

- [explain type ref_Mysql Explain 之 type 详解](https://blog.csdn.net/weixin_39882394/article/details/110648936)
- [Sql 执行计划，优化 sql 必备](https://blog.csdn.net/choath/article/details/80779129)
- [MySQL EXPLAIN 详解](https://www.cnblogs.com/aspirant/p/16166821.html)
- [一张图彻底搞懂 MySQL 的 explain](https://segmentfault.com/a/1190000021458117)
- []()
- []()
- []()

---

