# Redis

---

## 基础部分-数据类型

---

### Redis 的 String

**String**：

redis 的 String 可以表示任何数据，比如 jpg 图像或者序列化的对象。String 的最大值能存储 512 MB。

常用命令：

- set：往 redis 里输入 key-value。
- get：输入 key 值，返回对应的 value 值。
- incr：自增。
- decr：自减。
- mget：一次性获取多个 key 的 value。

### Redis 的 List

**List / 队列**：

List 的两端操作比较方便。

常用命令：

- lpush：从列表 List 的左边插入一个元素。
- lpop：从列表 List 的左边移出一个元素。
- rpush：从列表 List 的右边插入一个元素。
- rpop：从列表 List 的右边移出一个元素。
- llen：打印当前列表 List 中的元素个数。

### Redis 的 Set

**Set / 集合**：

常用命令：

- sadd：往 set 中添加数据。
- srem：从 set 中删除数据。
- scard：查看 set 中存在的元素个数。
- sismember：查看 set 中是否存在某个数据。

### Redis 的 Hash

**Hash / 哈希**：

常用命令：

- hget：通过 key 值，从 hash 里取对应的 value。
- hset：往 hash 里，添加 key-value。
- hmget：一次性获取多个 key 的 value。

### Redis 的 ZSet

**ZSet / 有序集合**：

常用命令：

- zadd：添加数据。
- zrem：删除元素。
    - zrem 还可以一次性删除多个元素。
- zcard：查询数据。
- zrange：数据排序，根据分数从小到大。
- zrevrange：数据排序， 根据分数从大到小。

---

## 基础部分-使用场景

---

### String 的使用场景

**String**：

普通的 key-value 键值对都可以用 String 来保存。

- 访问量统计，每次访问博客和文章，都用 incr 命令加一。
- 做缓存。
- 限流。
- 计数器。
- 分布式所。
- 分布式 Session。

### List 的使用场景

**List**：

作为队列，因为 List 的两端操作比较方便，所以可以用于一些需要获取最新数据的场景。

- 新闻类应用的最新新闻。
- 简单队列。
- 微博时间轴列表。

### Set 的使用场景

**Set**：

- 好友推荐，根据 Set 的内容求交集，大于某个阈值就可以推荐。
- 利用 Set 的唯一性，统计网站内所有独立 ip。
- 点赞。
- 点踩。
- 标签。
- 好友关系。

### Hash 的使用场景

**Hash**：

用于存储、修改对象属性。

- 用户（姓名、性别、爱好），文章（标题、发布时间、作者、内容）。
- 其中用户相当于 key，（姓名、性别、爱好）相当于存储的 value。
- 用户主页访问量。

### ZSet 的使用场景

**ZSet**：

zSet 本来就是有序的，并且有排序功能。

- 排行榜。

视频讲解：[Redis 集群架构实战核心技术（集群、事务、扩容、使用场景、分布式锁、源码架构、跳表原理、缓存数据库双写不一致）全集](https://www.bilibili.com/video/BV1PT4y1Z7K2?p=13)

微博热榜，维护微博热度前一百的 Sorted Set。

微博微信点赞，点赞集合按时间顺序排列。





---

参考链接：

- [Redis 的五种数据类型及其底层原理](https://blog.csdn.net/u014453898/article/details/112292028)
- [不能回滚的 Redis 事务还能用吗](https://www.cnblogs.com/lonely-wolf/p/14435075.html)
- [Redis 夺命十二问，你能扛到第几问？](https://mp.weixin.qq.com/s/ItIHbYsR4xiu2psUniN-5g)
- [谈谈redis的热key问题如何解决](https://www.cnblogs.com/rjzheng/p/10874537.html)
- []()
- []()
- []()

---
