# Java-collection / Java Collection 接口

---

## 基础部分

---

### Set 接口的实现

HashSet：

- 根据元素的 HashCode 做去重

TreeSet：

- 根据元素的 HashCode 做去重，并用平衡树进行排序

### Queue 接口的实现

PriorityQueue：

- 根据元素排序决定出列的顺序

### Collection 接口的继承关系图

<div align="center">
<img width="600"  alt="Collection 接口的继承关系" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/Java-collection.webp"/></div>

### 队列（Queue）的一些新方法

1. offer，add 区别：
    - 一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。这时新的 offer 方法就可以起作用了
    - 它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false


2. poll，remove 区别：
    - remove() 和 poll() 方法都是从队列中删除第一个元素
    - remove() 的行为与 Collection 接口的版本相似， 但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null
    - 因此新的方法更适合容易出现异常条件的情况


3. peek，element区别：
    - element() 和 peek() 用于在队列的头部查询元素
    - 与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null

---

## 提高部分

---

### 数组和链表实现队列哪个好

### 队列的特性

---








---

参考链接：

- []()

---













