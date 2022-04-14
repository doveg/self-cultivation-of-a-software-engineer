# Local Cache / 本地缓存

---

## 基础部分

---

### 根据 HashMap 自实现本地缓存

利用 LinkedHashMap 实现缓存的方式：

LinkedHashMap 维持了一个链表结构，用来存储节点的插入顺序或者访问顺序（二选一），并且内部封装了一些业务逻辑，只需要覆盖 removeEldestEntry 方法，便可以实现缓存的 LRU
淘汰策略。

此外我们利用读写锁，保障缓存的并发安全性。需要注意的是，这个示例并不支持过期时间淘汰的策略。

自实现缓存的方式，优点是实现简单，不需要引入第三方包，比较适合一些简单的业务场景。

缺点是如果需要更多的特性，需要定制化开发，成本会比较高，并且稳定性和可靠性也难以保障。

对于比较复杂的场景，建议使用比较稳定的开源工具。

### Guava Cache

Guava 是 Google 团队开源的一款 Java 核心增强库，包含集合、并发原语、缓存、IO、反射等工具箱，性能和稳定性上都有保障，应用十分广泛。

Guava Cache 支持很多特性：

    支持最大容量限制。
    支持两种过期删除策略（插入时间和访问时间）。
    支持简单的统计功能。
    基于 LRU 算法实现。

Guava Cache 是一款十分优异的缓存工具，功能丰富，线程安全，足以满足工程化使用，以上代码只介绍了一般的用法，实际上 springboot 对 guava
也有支持，利用配置文件或者注解可以轻松集成到代码中。

### Caffeine

Caffeine 是基于 java8 实现的新一代缓存工具，缓存性能接近理论最优。

可以看作是 Guava Cache 的增强版，功能上两者类似，不同的是 Caffeine 采用了一种结合 LRU、LFU 优点的算法：W-TinyLFU，在性能上有明显的优越性。

相比 Guava Cache 来说，Caffeine 无论从功能上和性能上都有明显优势。同时两者的 API 类似，使用 Guava Cache 的代码很容易可以切换到 Caffeine，节省迁移成本。

需要注意的是，SpringFramework5.0（SpringBoot2.0）同样放弃了 Guava Cache 的本地缓存方案，转而使用 Caffeine。

### Encache

Encache 是一个纯 Java 的进程内缓存框架，具有快速、精干等特点，是 Hibernate 中默认的 CacheProvider。

同 Caffeine 和 Guava Cache 相比，Encache 的功能更加丰富，扩展性更强：

    支持多种缓存淘汰算法，包括 LRU、LFU 和 FIFO。
    缓存支持堆内存储、堆外存储、磁盘存储（支持持久化）三种。
    支持多种集群方案，解决数据共享问题。

---

## 提高部分

---

###

---








---

参考链接：

- [Java本地缓存技术选型（Guava Cache、Caffeine、Encache）](https://www.jianshu.com/p/e5dc3a18dcb8)
- []()
- []()
- []()

---















