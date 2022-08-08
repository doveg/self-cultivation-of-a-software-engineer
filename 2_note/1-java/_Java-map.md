# Java-map / Java Map 接口

---

## Content / 目录

---

- [一、基础部分](#基础部分)
    - [01、Map 接口的继承关系图](#Map-接口的继承关系图)
    - [02、Map 接口的实现](#Map-接口的实现)
- [二、提高部分](#提高部分)
    - [01、HashMap 原理](#HashMap-原理)
    - [02、链表长度大于 8 会转成红黑树，为什么是 8](#链表长度大于-8-会转成红黑树，为什么是-8)
    - [03、红黑树](#红黑树)
    - [04、HashMap 扩容](#HashMap-扩容)
    - [05、HashMap 的装载因子为什是 0.75](#HashMap-的装载因子为什是-0.75)
    - [06、HashMap 长度为什么是 2 的 n 次方](#HashMap-长度为什么是-2-的-n-次方)
    - [07、HashMap 如何保持线程安全](#HashMap-如何保持线程安全)
    - [08、HashMap 的 put、get 原理](#HashMap-的-put、get-原理)
    - [09、HashMap 存储 10w 条数据，数据查找的时间复杂度](#HashMap-存储-10w-条数据，数据查找的时间复杂度)

---

## 基础部分

---

### Map 接口的继承关系图

<div align="center">
<img width="600"  alt="Map 接口的继承关系" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/Java-map.webp"/></div>

### Map 接口的实现

HashMap：

> 根据键值对的 key 做去重

TreeMap：

> 根据键值对的 key 做去重，并用平衡树进行排序

---

## 提高部分

---


---

## HashMap 原理

---

### HashMap 底层实现

视频讲解：[动画讲解 - hashMap 底层实现原理](https://www.bilibili.com/video/BV1Wh411n72X)

Java 8 的 HashMap 实现：

**数组 + LinkedList / 红黑树，如果数组长度大于64，或者链表长度大于 8，链表会转成红黑树。**

~~如果链表长度又小于 6 了，会重新把单向数据链表转成红黑树。~~

### 链表长度大于 8 会转成红黑树，为什么是 8

视频讲解：[HashMap 版本之间的区别](https://www.bilibili.com/video/BV1Sp4y1D732)

理想情况下，随机的 hashcode 算法，节点的分布会遵循泊松分布，链表长度达到 8 的概率非常小。

### 红黑树

红黑树的生成条件：

1、根节点是黑色的。

2、每个叶子节点到根节点最短路径上的黑色节点个数相同。

3、红色节点的两个子节点的时黑色的。

### HashMap 扩容

视频讲解：[HashMap 版本之间的区别](https://www.bilibili.com/video/BV1Sp4y1D732)

**让 hashcode 和原数组长度进行与操作，为零的位置不变，否则扩容后就是原数组加上原位置。**

### HashMap 的装载因子为什是 0.75

1、装载因子是为了解决 hash 冲突带来的链表过大的问题。

2、转载因子太小又会导致 HashMap 占用内存过多。0.75 是对 hash 冲突和 HashMap 占用内存两个妥协的产物。

### HashMap 长度为什么是 2 的 n 次方

视频讲解：[一个上小号的时间让你明白 HashMap 长度的原因](https://www.bilibili.com/video/BV13a4y1v7gR)

视频讲解：[HashMap 你很可能不知道的细节](https://www.bilibili.com/video/BV1534y1Q7CE)

**HashMap 的 key 值与 （HashMap 长度 - 1）进行与运算，2 的 n 次方可以方便取模。**

但是对于长度小的 HashMap，容易发生 hash 碰撞。

HashMap 在得到 hashcode 之后，和右移十六位后的值做了异或操作。（选十六位是因为 int 长度是三十二位）。

利用扰动函数避免只有低位的 key 值参与与运算的问题。

### HashMap 如何保持线程安全

HashMap 非线程安全，ConcurrentHashMap 能保证线程安全。

视频讲解：[动画讲解 - ConcurrentHashmap 的底层实现](https://www.bilibili.com/video/BV1Gq4y1Z7yM)

JDK 8 中，主要采用 CAS 思想，是乐观锁机制。

在更新数据时采用不断尝试的方式去更新。

**Synchronized 只锁数组中的一个节点**。

数据结构和 JDK 8 的 HashMap 一样。

ConcurrentHashMap 中，key 和 value 都不能为空。

### HashMap 的 put、get 原理

get 方法：

- get 操作是通过调用 getNode 方法来实现的，还是老规矩先对 key 进行 hash 计算，传入函数，返回 getNode 函数返回的值。
- 判断，如果 table 为空就返回空值。
- 不为空就先去看看第一个位置的节点 hash 值和 key 值是否相同。
- 果链表中不止一个节点那么就需要循环遍历了，如果存在多个 hash 碰撞这个是跑不掉的。
- 如果节点是树节点那么就使用树节点的 get 方法来取数。

### HashMap 存储 10w 条数据，数据查找的时间复杂度

理想情况下 HashMap 的时间复杂度为 O(1)。

考虑到数组下面有链表或者红黑树，在极端情况下可能接近 O(logN)。

但是每个哈希桶中元素数量是成泊松分布的，所以不太可能达到 O(logN)，通常认为 HashMap 的查询时间复杂度为 O(1)。

### HashMap 的 put 方法

首先会使用 hash 函数来计算 key，然后执行真正的插入方法。

put 方法核心就是 putval 方法，它的插入过程如下：

首先会判断 HashMap 中是否是新构建的，如果是的话会首先进行 resize

然后判断需要插入的元素在 HashMap 中是否已经存在（说明出现了碰撞情况），如果不存在，直接生成新的k-v 节点存放，再判断是否需要扩容。

如果要插入的元素已经存在的话，说明发生了冲突，这就会转换成链表或者红黑树来解决冲突，

    首先判断链表中的 hash，key 是否相等，如果相等的话，就用新值替换旧值，如果节点是属于 TreeNode 类型，会直接在红黑树中进行处理，
    如果 hash ,key 不相等也不属于 TreeNode 类型，会直接转换为链表处理，进行链表遍历，
    如果链表的 next 节点是 null，判断是否转换为红黑树，
    如果不转换的话，在遍历过程中找到 key 完全相等的节点，则用新节点替换老节点

---

---

参考链接：

- [HashMap 的 put、get 原理解读](https://www.jianshu.com/p/a3b64e18bfc6)
- [hashmap为什么查询时间复杂度为O(1)](https://blog.csdn.net/john1337/article/details/104727895)
- []()
- []()
- []()

---





