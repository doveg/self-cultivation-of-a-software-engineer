# Java-collection / Java Collection 接口

---

## Content / 目录

---

- [一、基础部分](#基础部分)
    - [01、Collection 接口的继承关系图](#Collection-接口的继承关系图)
    - [02、List 接口的实现](#List-接口的实现)
    - [03、Set 接口的实现](#Set-接口的实现)
    - [04、Queue 接口的实现](#Queue-接口的实现)
    - [05、Queue 的一些新方法](#Queue-的一些新方法)
- [二、提高部分](#提高部分)
    - [01、数组和链表实现队列哪个好](#数组和链表实现队列哪个好)
    - [02、队列的特性](#队列的特性)

---

## 基础部分

---

### Collection 接口的继承关系图

<div align="center">
<img width="600"  alt="Collection 接口的继承关系" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/Java-collection.webp"/></div>

### List 接口的实现

**LinkedList**：

- 双向链表；
- 由于继承了 AbstractSequentialList，也可以通过下标访问；
- 是 Deque 的实现类；可以作为双端队列，也可以用来作为栈。

**ArrayList**：

- 有序列表；
- 通过下标访问；
- 元素可以为空。

### Set 接口的实现

**HashSet**：

- 根据元素的 HashCode 做去重。

**TreeSet**：

- 根据元素的 HashCode 做去重，并用平衡树进行排序。

### Queue 接口的实现

**PriorityQueue**：

- 根据元素排序决定元素的顺序；
- 既可以根据元素的自然顺序来排序，也可以根据 Comparator 来设置排序规则；
- 实际上是一个堆、不指定 Comparator 时默认为最小堆。

PriorityQueue 实现原理：

Java 中 PriorityQueue 通过二叉小顶堆实现，可以用一棵完全二叉树表示（任意一个非叶子节点的权值，都不大于其左右子节点的权值），

也就意味着可以通过数组来作为 PriorityQueue 的底层实现。

我们给每个元素按照层序遍历的方式进行了编号，如果你足够细心，会发现父节点和子节点的编号是有联系的，更确切的说父子节点的编号之间有如下关系：

    leftNo = parentNo*2+1
    rightNo = parentNo*2+2
    parentNo = (nodeNo-1)/2

通过上述三个公式，可以轻易计算出某个节点的父节点以及子节点的下标。这也就是为什么可以直接用数组来存储堆的原因。

PriorityQueue 的 peek() 和 element 操作是常数时间，add(), offer(), 无参数的 remove() 以及 poll() 方法的时间复杂度都是 log(N)。

### Queue 的一些新方法

1、offer，add 区别：

- 一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。这时新的 offer 方法就可以起作用了
- 它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false

2、poll，remove 区别：

- remove() 和 poll() 方法都是从队列中删除第一个元素
- remove() 的行为与 Collection 接口的版本相似， 但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null
- 因此新的方法更适合容易出现异常条件的情况

3、peek，element区别：

- element() 和 peek() 用于在队列的头部查询元素
- 与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null

---

## 提高部分

---

### 数组和链表实现队列哪个好

链表

### 队列的特性

FIFO / 先入先出

---


---

参考链接：

- [java集合Collection常用方法详解](https://blog.csdn.net/javaee_gao/article/details/96372530)
- [Java 集合系列05之 LinkedList详细介绍(源码解析)和使用示例](https://www.cnblogs.com/skywang12345/p/3308807.html)
- [java队列——queue详细分析](https://www.cnblogs.com/lemon-flm/p/7877898.html)
- [深入理解 Java PriorityQueue](https://www.cnblogs.com/CarpenterLee/p/5488070.html)
- [PriorityQueue的用法和底层实现原理](https://blog.csdn.net/u010623927/article/details/87179364)
- []()
- []()
- []()

---

