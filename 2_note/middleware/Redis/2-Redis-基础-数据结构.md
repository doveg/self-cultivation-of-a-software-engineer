# Redis

---

## 基础部分-跳表和压缩表

---

### 跳表是什么

视频讲解：[学会跳表只需要 3 分钟 | 让你的链表支持二分查找](https://www.bilibili.com/video/BV1QK4y1Y7mS)

**跳表**：

**为链表加索引，让链表支持二分查找。**

视频讲解：[动画讲解 - 跳跃表原理](https://www.bilibili.com/video/BV1xv41137Yj)

~~第一层存储所有的元素，第二层索引的格式时第一层的二分之一。~~

~~从最底层开始，上一层的索引对应下一层的奇数下标点，生成这一级的索引。~~

### Redis 为什么用跳表不用树

视频讲解：[讲讲 Zset 底层数据原理？为什么不用树而用跳表](https://www.bilibili.com/video/BV1kh411x7Jc)

1、查找性能比红黑树要快；

2、实现比红黑树等平衡二叉树要简单。

### 压缩表是什么

视频讲解：[Redis底层数据结构 压缩列表](https://www.bilibili.com/video/BV1ot4y1e7Mz)

**压缩列表**：

是 Redis 为了节约内存而开发的，是由一系列特殊编码的连续内存块组成的顺序型 (sequential) 数据结枃。

**一个压缩列表可以包含任意多个节点 (entry), 每个节点可以保存一个字节数组或者一个整数值。**

添加新节点到压缩列表，或者从压缩列表中删除节点，可能会引发连锁更新操作，但这种操作出现的几率并不高。

结构：

> zlbytes: 记录整个压缩列表使用的内存大小
>
> zltail: 记录压缩列表表尾距离起始位置有多少字节
>
> zllen: 记录压缩列表节点数量，值得注意的一点是，因为它只占了 2 个字节，所以最大值只能到 65535，这意味着压缩列表长度大于 65535 的时候，就只能通过遍历整个列表来计算长度了
>
> zleng: 压缩列表末端标志位，固定值为 OxFF
>
> entry1-N:
>
> > previous_entry_length: 上一个节点的长度
> >
> > encoding: content 的编码以及长度
> >
> > content: 节点数据

---

## 基础部分-底层数据结构

---

### redisObject 内部结构

Redis 内部使用一个 redisObject 对象来表示所有的 key 和 value

redisObject 内部结构：

type：

- 用来表示这个 redisObject 是属于五种类型 (string、hash、list、set、zset) 的哪一种
- 比如 type=string 代表 value 存储的是一个普通字符串

encoding：

- 用来表示 type 的底层数据结构是用什么实现的
- 打个比方，就如 Java 中的 list ，可以由 ArrayList 来实现，也可以由 LinkedList 来实现

ptr：

- 指向底层数据结构的指针

vm： - 只有打开了 Redis 的虚拟内存功能，此字段才会真正的分配内存，该功能默认是关闭状态的

<div align="center">
<img width="600"  alt="redisObject 内部结构" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/middleware/redisObject内部结构.bmp"/></div>

### redisObject 如何表示 string

字符串的编码方式有三种：

int：

- 当 string 对象的值全部是数字，就会使用 int 编码。

raw：

- 如果字符串 string 保存的是一个字符串值，并且这个字符串大于 39 个字节。
- 那么字符串对象将使用一个简单动态字符串 (SDS) 来保存这个字符串值。
- 并将 redisObject 的 encoding 设置为 raw。

embstr：

- 如果字符串 string 保存的是一个字符串值，并且这个字符串小于 39 个字节，那么字符串将使用 embstr 编码的方式来保存这个字符串。

### redisObject 如何表示 list

列表对象 list 的编码方式有两种：

ziplist：

- 压缩列表是节省内存而设计的内存结构（是 redis 创造的）。
- 优点：节省内存。
- 缺点：比其他结构要消耗更多的时间。
- 所以 redis 在数据量少的时候使用压缩列表存储。

linkedlist：

- 当列表长度少于 512 且每个元素都少于 64 个字节，那么就用 ziplist 存储。
- 否则就用 linkedlist 存储。

### redisObject 如何表示 hash

hash 的 encoding 编码方式有两种：

ziplist：

- 当哈希对象保存的键值对数量少于 512，且所有键值对的长度都少于 64 字节时，使用压缩列表保存。

hashtable：

- 当哈希对象保存的键值对个数大于 512，并且其中有键值对大于 64 个字节，就使用 hashtable 保存。

### redisObject 如何表示 set

set 的 encoding 编码方式有两种：

intset：

- 集合的长度少于 512 时，并且所有元素都是整数，使用 intset 存储。
- 否则使用 hashtable。

hashtable：

- hashtable 编码的底层实现是字典，字典的每个键是字符串对象，只不过值都是空 (NULL)。

### redisObject 如何表示 zset

zset / SortedSet 的 encoding 编码方式有两种：

1、**ziplist / 压缩表**：

- 当 **zset 的长度少于 128**，并且**所有元素的长度都少于 64 字节**时，用 ziplist 存储。
- 否则用 skiplist 存储。

2、**skiplist / 跳表**：

- redis 的 skiplist 是由字典 dict 和跳表构成的。
- dict 用于记录字符串对象和分数，即查询字符串对象对应分数。
- 跳表则用来，根据分数查询对应字符串。

为什么 skiplist 编码要同时用字典和跳表来实现：

> 字典查询分值的时间复杂度是 O (1)，但是无序。
>
> 跳表的优点是有序，但是查询的时间复杂度为 O (logn)。
>
> 虽然采用两个结构，但是集合的元素成员和分值是共享的，两种结构都通过指针指向同一地址，所以不会存在内存浪费。






---

