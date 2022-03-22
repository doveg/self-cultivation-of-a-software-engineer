# Java-map / Java Map 接口

---

## 基础部分

---

### Map 接口的实现

HashMap：

- 根据键值对的 key 做去重

TreeMap：

- 根据键值对的 key 做去重，并用平衡树进行排序

### Map 接口的继承关系图

<div align="center">
<img width="600"  alt="Map 接口的继承关系" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/Java-map.webp"/></div>

---

## 提高部分

---


---

## HashMap 原理

---

### HashMap 底层实现

视频讲解：[动画讲解 - hashMap 底层实现原理](https://www.bilibili.com/video/BV1Wh411n72X)

Java 8 的 HashMap 实现：

数组 + LinkedList / 红黑树，如果数组长度大于64，或者链表长度大于 8，链表会转成红黑树。

### 链表长度大于 8 会转成红黑树，为什么是 8

视频讲解：[HashMap 版本之间的区别](https://www.bilibili.com/video/BV1Sp4y1D732)

理想情况下，随机的 hashcode 算法，节点的分布会遵循泊松分布。

其实链表长度达到 8 的概率非常小。

### 红黑树

红黑树的生成条件：

1、根节点是黑色的。

2、每个叶子节点到根节点最短路径上的黑色节点个数相同。

3、红色节点的两个子节点的时黑色的。

### HashMap 扩容

视频讲解：[HashMap 版本之间的区别](https://www.bilibili.com/video/BV1Sp4y1D732)

让 hashcode 和原数组长度进行与操作，为零的位置不变，否则扩容后就是原数组加上原位置。

### HashMap 如何保持线程安全

### HashMap put、get 原理

### HashMap 为什是 0.75 的装载因子 -> 主要是解决什么问题的

装载因子是为了解决 hash 冲突带来的链表过大的问题。

转载因子太小又会导致 HashMap 占用内存过多。

0.75 是对 hash 冲突和 HashMap 占用内存两个妥协的产物。

### HashMap 存储 10w 条数据，数据查找的时间复杂度

理想情况下 HashMap 的时间复杂度为 O（1）。

但是考虑到数组下面有链表或者红黑树。

### HashMap 长度为什么是 2 的 n 次方

视频讲解：[一个上小号的时间让你明白 HashMap 长度的原因](https://www.bilibili.com/video/BV13a4y1v7gR)

视频讲解：[HashMap 你很可能不知道的细节](https://www.bilibili.com/video/BV1534y1Q7CE)

HashMap 的 key 值与 （HashMap 长度 - 1）进行与运算，2 的 n 次方可以方便取模。

但是对于长度小的 HashMap，容易发生 hash 碰撞。

HashMap 在得到 hashcode 之后，和右移十六位后的值做了异或操作。（选十六位是因为 int 长度是三十二位）。

利用扰动函数避免只有低位的 key 值参与与运算的问题。


---







---

参考链接：

- []()

---











