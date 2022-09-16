# Go

---

## 基础

---

### 对比 Java

1. 性能上：

golang 的性能比 Java 更好，占用内存更少，使用 goroutine 避免内核态和用户态切换成本。

2. 编译部署：

Java 通过虚拟机编译，使用 JVM 跨平台编译；

Go 中不存在虚拟机，针对不同的平台，编译对应的机器码；

3. 访问权限:

java 使用 public、protected、private、默认等关键字；

golang 通过大小写控制。

4. 接口:

java 等面向对象编程的接口是侵入式接口，需要明确声明自己实现了某个接口。

而 Golang 的非侵入式接口不需要通过任何关键字，只要一个类型实现了接口的所有方法，就是这个接口的实现。

5. 异常处理:

java 中错误（Error）和异常 (Exception) 被分类管理，golang 中只有 error，一旦发生错误逐层返回，直到被处理。

6. 继承：

Java 的继承通过 extends 关键字完成，不支持多继承；Go 语言的继承通过 Struct 的方式，子类只需要把基类作为成员放在子类的定义中，支持多继承。

7. 多态:

java 的多态，必须满足继承，重写，向上转型；

在 Go 语言中通过接口实现多态，对接口的实现只需要某个类型 T 实现了接口中的方法，就相当于实现了该接口。

8. 值传递和引用传递:

java 中不存在显式的指针，而 Golang 中存在显式的指针操作。

java 和 golang 都是只存在值传递。

9. 并发：

在 Java 中，通常借助于共享内存（全局变量）作为线程间通信的媒介，通常会有线程不安全问题，使用了加锁（同步化）、使用原子类、使用 volatile 提升可见性等解决；

但在 Golang 中使用的是通道（channel）作为协程间通信的媒介，多个 goroutine 之间通过 Channel 来通信，chan 的读取和写入操作为原子操作，所以是安全的。

10. 垃圾回收和内存管理机制：

Java 基于 JVM 虚拟机的分代收集算法完成 GC，golang 内存释放语言层面，对不再使用的内存资源进行自动回收，多级缓存，非分代，并发的三色标记算法。

### 协程和线程

可以在单个线程上运行多个协程，因为协程支持挂起，不会使正在运行协程的线程阻塞。

挂起比阻塞节省内存，且支持多个并行操作。

### 优势

节省内存：

    协程
    编译成机器码
    垃圾回收和内存管理机制

节省服务器成本

Channel 通信的原子性操作

### 特性

显式的指针

异常

关键字：

    继承
    实现接口

---

##

---

###

###

###

---

参考链接：

- [说说 golang 和 java 有哪些区别](https://juejin.cn/post/7041935236149542926)
- []()
- []()
- []()

---









