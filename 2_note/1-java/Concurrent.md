# Concurrent

---

## 基础部分

---

### JUC 包的主要内容

JUC：JDK 中 java.util.concurrent 工具包的简称。

JUC-locks 锁框架：

> 接口说明
>
> ReentrantLock 的使用
>
> ReentrantReadWriteLock 的使用
>
> LockSupport 工具类
>
> AbstractQueuedSynchronizer 综述
>
> AbstractQueuedSynchronizer 的独占功能原理
>
> Condition 原理
>
> AbstractQueuedSynchronizer 的共享功能原理
>
> ReentrantReadWriteLock 原理
>
> 更强的读写锁 ——StampedLock

JUC-atomic 原子类框架：

> Unsafe 类
>
> AtomicInteger
>
> AtomicReference
>
> Atomic 数组
>
> AtomicXXXFieldUpdater
>
> 更强的原子类 ——LongAdder

JUC-sync 同步器框架：

> 倒数计数器 ——CountDownLatch
>
> 循环栅栏 ——CyclicBarrier
>
> 信号量 ——Semaphore
>
> 数据交换器 ——Exchanger
>
> 多阶段栅栏 ——Phaser

JUC-collections 集合框架：

> ConcurrentHashMap 的基本原理
>
> ConcurrentHashMap 的扩容
>
> 跳表 ——ConcurrentSkipListMap
>
> ConcurrentSkipListSet
>
> “写时复制” 的应用 ——CopyOnWriteArrayList
>
> CopyOnWriteArraySet
>
> 无锁队列 ——ConcurrentLinkedQueue
>
> 无锁双端队列 ——ConcurrentLinkedDeque
>
> 阻塞队列简介 ——BlockingQueue
>
> 基于数组的阻塞队列 ——ArrayBlockingQueue
>
> 基于单链表的阻塞队列 ——LinkedBlockingQueue
>
> 基于堆的优先级阻塞队列 ——PriorityBlockingQueue
>
> 特殊的同步队列 ——SynchronousQueue
>
> 延时阻塞队列 ——DelayQueue
>
> 基于双链表的阻塞双端队列 ——LinkedBlockingDeque

JUC-executors 执行器框架：

> executors 框架概述
>
> 普通线程池 ——ThreadPoolExecutor
>
> 计划线程池 ——ScheduledThreadPoolExecutor
>
> Future 模式
>
> Fork/Join 框架的原理
>
> Fork/Join 框架的实现

### 线程安全的原因

### 解决线程安全问题的常用方案

### 多线程，要求输出 AAB、 BAA、 ABA

### Unsafe 类

---

## 锁

---

### 锁的四种状态

> 无锁
>
> 偏向锁：Java 6 引入。
>
> 轻量级锁：Java 6 引入。
>
> 重量级锁

锁只能升级，不能降级。

### 锁的使用

互斥锁：

自旋锁：自旋，相当于 cpu 的空转，优化版本是适应性自选。

悲观锁：

乐观锁：

### CAS / Compare and Swap / 比较并交换

视频讲解：[【Java 并发】面试官问我 CAS、乐观锁、悲观锁，我反手就是骑脸输出](https://www.bilibili.com/video/BV1ff4y1q7we) （推荐）

CAS 是一种无锁算法，CAS 有 3 个操作数：内存值 V，旧的预期值 A，要修改的新值 B。

当且仅当预期值 A 和内存值 V 相同时，将内存值 V 修改为 B，否则什么都不做。

CAS（比较并交换）是 CPU 指令级的操作，只有一步原子操作（因为时一条指令），所以非常快。而且 CAS 避免了请求操作系统来裁定锁的问题，不用麻烦操作系统，直接在 CPU 内部就搞定了。

在实际应用中，如果 CAS 失败，会让线程自旋，不断重试；通常会配置自旋次数来避免死循环。

在原子类变量中，如 java.util.concurrent.atomic 中的 AtomicXXX，都使用了这些底层的 JVM 支持为数字类型的引用类型提供一种高效的 CAS 操作，而在
java.util.concurrent 中的大多数类在实现时都直接或间接的使用了这些原子变量类。

AtomicInteger.incrementAndGet 的实现用了乐观锁技术，调用了类 sun.misc.Unsafe 库里面的 CAS 算法，用 CPU
指令来实现无锁自增。所以，AtomicInteger.incrementAndGet 的自增比用 synchronized 的锁效率倍增。

### AQS / AbstractQueuedSynchronizer / 抽象队列同步器

AQS 中 维护了一个 volatile int state（代表共享资源）和一个 FIFO 线程等待队列（多线程争用资源被阻塞时会进入此队列）。

这里 volatile 能够保证多线程下的可见性，当 state=1 则代表当前对象锁已经被占有，其他线程来加锁时则会失败，加锁失败的线程会被放入一个 FIFO 的等待队列中，并且会被
UNSAFE.park() 操作挂起，等待其他获取锁的线程释放锁才能够被唤醒。

另外 state 的操作都是通过 CAS 来保证其并发修改的安全性。

AQS 队列主要是基于非公平锁的独占锁实现。

在获得同步锁时，同步器维护一个同步队列，获取状态失败的线程都会被加入到队列中并在队列中进行自旋；移出队列（或停止自旋）的条件是前驱节点为头节点且成功获取了同步状态。

在释放同步状态时，同步器调用 tryRelease (int arg) 方法释放同步状态，然后唤醒头节点的后继节点。

核心方法：

> tryAcquire：尝试获取锁，获取不到直接返回。
>
> acquire：获取锁，获取不到进入队列进行争抢，直到获取到锁为止。

### Condition

Condition 是在 java 1.5 中才出现的，它用来替代传统的 Object 的 wait()、notify() 实现线程间的协作，相比使用 Object 的 wait()、notify()
，使用 Condition 中的 await()、signal() 这种方式实现线程间协作更加安全和高效。因此通常来说比较推荐使用 Condition

其中 AbstractQueueSynchronizer 中实现了 Condition 中的方法，主要对外提供 awaite(Object.wait()) 和 signal(
Object.notify()) 调用。

### ReentrantLock / 可重入锁

核心方法：

> lock 方法：获取锁。
>
> lockInterruptibly 方法：获取锁。如果不能获取到，只有进行等待的情况下，是可以响应中断的。
>
> tryLock 方法：获取锁。

### ReentrantReadWriteLock / 可重入读写锁

ReentrantLock 是独占锁，某一时刻只有一个线程可以获取该锁，而实际中会有写少读多的场景，显然 ReentrantLock 满足不了这个需求，所以
ReentrantReadWriteLock 应运而生。

ReentrantReadWriteLock 采用读写分离的策略，允许多个线程可以同时获取读锁。

### Synchronized / 同步锁

关键字 Synchronized，通过监视器实现同步锁，是重量级锁。

synchronized 基本上都是排它锁，意味着这些锁在同一时刻只允许一个线程进行访问，而读写锁在同一时刻可以允许多个读线程访问，在写线程访问的时候其他的读线程和写线程都会被阻塞。

读写锁维护一对锁（读锁和写锁），通过锁的分离，使得并发性提高。

所以对线程安全有要求，并且并发性能要求高的场景，一般采用 ReentrantReadWriteLock（读写锁）。

### 什么是死锁

指多个进程在运行过程中因争夺资源而造成的一种僵局，当进程处于这种僵持状态时，若无外力作用，它们都将无法再向前推进。

### 死锁的原因

死锁产生的原因主要包括：

> 系统资源不足；
>
> 程序执行的顺序有问题；
>
> 资源分配不当等。

如果系统资源充足，进程的资源请求都能够得到满足，那么死锁出现的可能性就很低；否则，

就会因争夺有限的资源而陷入死锁。其次，程序执行的顺序与速度不同，也可能产生死锁。

### 死锁的四个必要条件

视频讲解：[花三分钟了解一下死锁 | 这波不亏，不亏](https://www.bilibili.com/video/BV1av411q73F)

> **互斥**：一个资源每次只能被一个线程使用。
>
> **请求与保持**：一个线程因请求资源而阻塞时，对已获得的资源保持不放。
>
> **不剥夺**：线程已获得的资源，在未使用完之前，不能强行剥夺。
>
> **循环等待**：若干线程之间形成一种头尾相接的循环等待资源关系。

分析：

> 互斥条件 ---> 独占锁的特点之一。
>
> 请求与保持条件 ---> 独占锁的特点之一，尝试获取锁时并不会释放已经持有的锁
>
> 不剥夺条件 ---> 独占锁的特点之一。
>
> **循环等待条件** ---> **唯一需要记忆的造成死锁的条件**。

### 如何避免死锁

1、以确定的顺序获得锁：

如果必须获取多个锁，那么在设计的时候需要充分考虑不同线程之前获得锁的顺序。

针对两个特定的锁，开发者可以尝试按照锁对象的 hashCode 值大小的顺序，分别获得两个锁，这样锁总是会以特定的顺序获得锁，那么死锁也不会发生。

问题变得更加复杂一些，如果此时有多个线程，都在竞争不同的锁，简单按照锁对象的 hashCode 进行排序（单纯按照 hashCode 顺序排序会出现
“环路等待”），可能就无法满足要求了，这个时候开发者可以使用银行家算法，所有的锁都按照特定的顺序获取，同样可以防止死锁的发。

2、超时放弃：

当使用 synchronized 关键词提供的内置锁时，只要线程没有获得锁，那么就会永远等待下去，然而 Lock 接口提供了 boolean tryLock (long time, TimeUnit
unit) throws InterruptedException
方法，该方法可以按照固定时长等待锁，因此线程可以在获取锁超时以后，主动释放之前已经获得的所有的锁。通过这种方式，也可以很有效地避免死锁。

3、银行家算法：

银行家算法：首先需要定义状态和安全状态的概念。系统的状态是当前给进程分配的资源情况。

因此，状态包含两个向量 Resource（系统中每种资源的总量）和 Available（未分配给进程的每种资源的总量）及两个矩阵 Claim（表示进程对资源的需求）和
Allocation（表示当前分配给进程的资源）。

安全状态是指至少有一个资源分配序列不会导致死锁。

当进程请求一组资源时，假设同意该请求，从而改变了系统的状态，然后确定其结果是否还处于安全状态。

如果是，同意这个请求；如果不是，阻塞该进程知道同意该请求后系统状态仍然是安全的。

### 死锁的解决方式

如果利用死锁检测算法检测出系统已经出现了死锁 ，那么，此时就需要对系统采取相应的措施。

常用的解除死锁的方法：

> 1、抢占资源：从一个或多个进程中抢占足够数量的资源分配给死锁进程，以解除死锁状态。
>
> 2、终止（或撤销）进程：终止或撤销系统中的一个或多个死锁进程，直至打破死锁状态。
>
> > a、终止所有的死锁进程。这种方式简单粗暴，但是代价很大，很有可能会导致一些已经运行了很久的进程前功尽弃。
> >
> > b、逐个终止进程，直至死锁状态解除。该方法的代价也很大，因为每终止一个进程就需要使用死锁检测来检测系统当前是否处于死锁状态。另外，每次终止进程的时候终止那个进程呢？每次都应该采用最优策略来选择一个 “代价最小” 的进程来解除死锁状态。
> >
> > 一般根据如下几个方面来决定终止哪个进程：
> >
> > > 进程的优先级
> > >
> > > 进程已运行时间以及运行完成还需要的时间
> > >
> > > 进程已占用系统资源
> > >
> > > 进程运行完成还需要的资源
> > >
> > > 终止进程数目
> > >
> > > 进程是交互还是批处理

###

---

## 数据结构

---

### ThreadLocal / 线程局部变量 / 线程本地变量

线程私有变量，避免其他线程对对象进行修改。

基于 ThreadLocalMap 实现，ThreadLocal 存在栈中，值是 ThreadLocalMap 在堆中的地址。

ThreadLocalMap 的 entry 继承了弱引用。

ThreadLocal 的核心方法：get / set / remove。

ThreadLocal 用完之后要在 finally remove 掉。

### 强引用、软引用、弱引用、虚引用

根据 GC Root 判断对象是否可达。

强引用：

软引用：

弱引用：

虚引用:

### ConcurrentHashMap

视频讲解：[动画讲解 - ConcurrentHashmap 的底层实现](https://www.bilibili.com/video/BV1Gq4y1Z7yM)

JDK 8 中，主要采用 CAS 思想，是乐观锁机制。

在更新数据时采用不断尝试的方式去更新。

**Synchronized 只锁数组中的一个节点**。

数据结构和 JDK 8 的 HashMap 一样。

ConcurrentHashMap 中，key 和 value 都不能为空。

### BlockingQueue / 阻塞队列

是一个支持两个附加操作的队列。









---

参考链接：

- [什么是死锁，产生死锁的原因及必要条件](https://blog.csdn.net/hd12370/article/details/82814348)
- [死锁的四个必要条件和解决办法](https://blog.csdn.net/guaiguaihenguai/article/details/80303835)
- [透彻理解 Java 并发编程系列](https://segmentfault.com/a/1190000015558984)
- [CAS 原理](https://www.jianshu.com/p/ab2c8fce878b)
- [【深入 AQS 原理】我画了 35 张图就是为了让你深入 AQS](https://juejin.cn/post/6844904146127044622)
- [Java 技术之 AQS 详解](https://www.jianshu.com/p/da9d051dcc3d)
- [读写锁 ——ReentrantReadWriteLock 原理详解](https://cloud.tencent.com/developer/article/1469555)
- []()

---



