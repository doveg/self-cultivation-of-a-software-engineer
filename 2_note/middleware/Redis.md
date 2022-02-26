# Redis

---

## 基础部分

---

### Redis 五大数据类型

**String**：

- redis 的 String 可以表示任何数据，比如 jpg 图像或者序列化的对象
- String 的最大值能存储 512 MB
- 常用命令：
    - set：往 redis 里输入 key-value
    - get：输入 key 值，返回对应的 value 值
    - incr：自增
    - decr：自减
    - mget：一次性获取多个 key 的 value

**List**：

- 队列，List 的两端操作比较方便
- 常用命令：
    - lpush：从列表 List 的左边插入一个元素
    - lpop：从列表 List 的左边移出一个元素
    - rpush：从列表 List 的右边插入一个元素
    - rpop：从列表 List 的右边移出一个元素
    - llen：打印当前列表 List 中的元素个数

**Set**：

- 集合
- 常用命令：
    - sadd：往 set 中添加数据
    - srem：从 set 中删除数据
    - scard：查看 set 中存在的元素个数
    - sismember：查看 set 中是否存在某个数据

**Hash**：

- 哈希
- 比如：
    - hget：通过 key 值，从 hash 里取对应的 value
    - hset：往 hash 里，添加 key-value
    - hmget：一次性获取多个 key 的 value

**ZSet**：

- 有序集合
- 例如：
    - zadd：添加数据
    - zrem：删除元素
        - zrem 还可以一次性删除多个元素：
    - zcard：查询数据
    - zrange：数据排序，根据分数从小到大
    - zrevrange：数据排序， 根据分数从大到小

### Redis 五大数据类型的使用场景

**String**：

- 普通的 key-value 键值对都可以用 String 来保存
- 例如：
    - 访问量统计，每次访问博客和文章，都用 incr 命令加一
    - 做缓存
    - 限流
    - 计数器
    - 分布式所
    - 分布式 Session

**List**：

- 作为队列，因为 List 的两端操作比较方便，所以可以用于一些需要获取最新数据的场景
- 例如：
    - 新闻类应用的最新新闻
    - 简单队列
    - 微博时间轴列表

**Set**：

- 例如：
    - 好友推荐，根据 Set 的内容求交集，大于某个阈值就可以推荐
    - 利用 Set 的唯一性，统计网站内所有独立 ip
    - 点赞
    - 点踩
    - 标签
    - 好友关系

**Hash**：

- 用于存储、修改对象属性
- 例如：
    - 用户（姓名、性别、爱好），文章（标题、发布时间、作者、内容）
    - 其中用户相当于 key，（姓名、性别、爱好）相当于存储的 value
    - 用户主页访问量

**ZSet**：

- zSet 本来就是有序的，并且有排序功能
- 例如：排行榜

---

## 提高部分

---

### Redis 事务和 ACID 属性

**原子性（atomicity）**：

Redis 不支持回滚。

Redis 命令在事务执行时可能会失败，但仍会继续执行剩余命令而不是 Rollback（事务回滚）。如果你使用过关系数据库，这种情况可能会让你感到很奇怪。

来自官网的解释：

- Redis 作者认为发生事务回滚的原因大部分都是程序错误导致，这种情况一般发生在开发和测试阶段，而生产环境很少出现。
- 对于逻辑性错误，比如本来应该把一个数加 1 ，但是程序逻辑写成了加 2，那么这种错误也是无法通过事务回滚来进行解决的。
- Redis 追求的是简单高效，而传统事务的实现相对比较复杂，这和 Redis 的设计思想相违背。

**一致性（consistency）**：

一致性是事务执行前后的数据符合数据库的定义和要求。

这一点 Redis 中的事务是符合要求的，不论是发生语法错误还是运行时错误，错误的命令均不会被执行。

**隔离性（isolation）**：

事务中的所有命令都会按顺序执行。

在执行 Redis 事务的过程中，另一个客户端发出的请求不可能被服务，这保证了命令是作为单独的独立操作执行的。所以 Redis 当中的事务是符合隔离性要求的。

**持久性（durability）**：

- 如果 Redis 当中没有被开启持久化，那么就是纯内存运行的，一旦重启，所有数据都会丢失，此时可以认为 Redis 不具备事务的持久性；
- 而如果 Redis 开启了持久化，那么可以认为 Redis 在特定条件下是具备持久性的。

### Redis 底层原理 - redisObject 内部结构

Redis 内部使用一个 redisObject 对象来表示所有的 key 和 value

redisObject 内部结构：

- type：
    - 用来表示这个 redisObject 是属于五种类型 (string、hash、list、set、zset) 的哪一种
    - 比如 type=string 代表 value 存储的是一个普通字符串


- encoding：
    - 用来表示 type 的底层数据结构是用什么实现的
    - 打个比方，就如 Java 中的 list ，可以由 ArrayList 来实现，也可以由 LinkedList 来实现


- ptr：
    - 指向底层数据结构的指针


- vm：
    - 只有打开了 Redis 的虚拟内存功能，此字段才会真正的分配内存，该功能默认是关闭状态的

<div align="center">
<img width="600"  alt="redisObject 内部结构" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/middleware/redisObject内部结构.bmp"/></div>

### Redis 底层原理 - redisObject 如何表示 string

字符串的编码方式有三种：

- int：
    - 当 string 对象的值全部是数字，就会使用 int 编码


- raw：
    - 如果字符串 string 保存的是一个字符串值，并且这个字符串大于 39 个字节
    - 那么字符串对象将使用一个简单动态字符串 (SDS) 来保存这个字符串值
    - 并将 redisObject 的 encoding 设置为 raw


- embstr：
    - 如果字符串 string 保存的是一个字符串值，并且这个字符串小于 39 个字节，那么字符串将使用 embstr 编码的方式来保存这个字符串

### Redis 底层原理 - redisObject 如何表示 list

列表对象 list 的编码方式有两种：

- ziplist：
    - 压缩列表是节省内存而设计的内存结构（是 redis 创造的）
    - 优点：节省内存
    - 缺点：比其他结构要消耗更多的时间
    - 所以 redis 在数据量少的时候使用压缩列表存储


- linkedlist：
    - 当列表长度少于 512 且每个元素都少于 64 个字节，那么就用 ziplist 存储
    - 否则就用 linkedlist 存储

### Redis 底层原理 - redisObject 如何表示 hash

hash 的 encoding 编码方式有两种：

- ziplist：
    - 当哈希对象保存的键值对数量少于 512，且所有键值对的长度都少于 64 字节时，使用压缩列表保存


- hashtable：
    - 当哈希对象保存的键值对个数大于 512，并且其中有键值对大于 64 个字节，就使用 hashtable 保存

### Redis 底层原理 - redisObject 如何表示 set

set 的 encoding 编码方式有两种：

- intset：
    - 集合的长度少于 512 时，并且所有元素都是整数，使用 intset 存储
    - 否则使用 hashtable


- hashtable：
    - hashtable 编码的底层实现是字典，字典的每个键是字符串对象，只不过值都是空 (NULL)

### Redis 底层原理 - redisObject 如何表示 zset

zset 的 encoding 编码方式有两种：

- ziplist：
    - 当 zset 的长度少于 128，并且所有元素的长度都少于 64 字节时，用 ziplist 存储
    - 否则用 skiplist 存储


- skiplist：
    - redis 的 skiplist 是由字典 dict 和跳表构成的
        - dict 用于记录字符串对象和分数，即查询字符串对象对应分数
        - 跳表则用来，根据分数查询对应字符串。

为什么 skiplist 编码要同时用字典和跳表来实现：

- 字典查询分值的时间复杂度是 O (1)，但是无序
- 跳表的优点是有序，但是查询的时间复杂度为 O (logn)
- 虽然采用两个结构，但是集合的元素成员和分值是共享的，两种结构都通过指针指向同一地址，所以不会存在内存浪费

---








---

参考链接：

- [Redis 的五种数据类型及其底层原理](https://blog.csdn.net/u014453898/article/details/112292028)
- [不能回滚的 Redis 事务还能用吗](https://www.cnblogs.com/lonely-wolf/p/14435075.html)
- []()

---















