# Thread-pool / 线程池

---

## 线程

---

### 线程的状态

1、初始 / new：线程被构建。

2、运行 / runnable：操作系统中的就绪和运行两种状态。

3、阻塞 / blocked：线程被锁阻塞。

4、等待 / waiting：当前线程需要其他线程通知。

5、定时等待 / timed_waiting：指定等待时间自己返回。

6、终止 / terminated：当前线程执行完毕。

### 创建线程的方式

1、继承 Thread 类，并重写 run () 方法。

    首先定义一个类来继承 Thread 类，重写 run 方法。
    然后创建这个子类对象，并调用 start 方法启动线程。

2、实现 Runnable，并实现 run () 方法。

    首先定义一个类实现 Runnable 接口，并实现 run 方法。
    然后创建 Runnable 实现类对象，并把它作为 target 传入 Thread 的构造函数中。
    最后调用 start 方法启动线程。

3、实现 Callable 接口，并结合 Future 实现。

    首先定义一个 Callable 的实现类，并实现 call 方法。call 方法是带返回值的。
    然后通过 FutureTask 的构造方法，把这个 Callable 实现类传进去。
    把 FutureTask 作为 Thread 类的 target ，创建 Thread 线程对象。
    通过 FutureTask 的 get 方法获取线程的执行结果。

4、使用线程池，例如用 JDK 自带的 Executors 创建线程池对象。

    首先，定一个 Runnable 的实现类，重写 run 方法。
    然后创建一个拥有固定线程数的线程池。
    最后通过 ExecutorService 对象的 execute 方法传入线程对象。

### Runnable 和 Thread 的关系

1、Thread 类是 Runnable 接口的实现类，继承 Thread 类时覆写的还是 Runnable 接口的 run () 方法。

2、使用 Runnable 是最方便的，因为其可以避免单继承的局限，同时也可以更好进行功能的扩充。

### Runnable、Callable 和 Future 的关系

1、Runnable：

> Runnable.run () 方法没有返回值。
>
> 不会向外抛异常。

2、Callable：

> Callable.call () 方法有返回值。
>
> 可以向外抛异常。

3、Future：

> 因为 Thread 不接受 Callable，只接受 Runnable，所以有了 FutureTask。
>
> FutureTask 是 Runnable 和 Future 的实现类：FutureTask 继承了 RunnableFuture，RunnableFuture 实现了 Runnable 和 Future 接口。

---

## 基础部分

---

### 线程池的核心配置参数（重点）

最重要的：

1、**核心线程数**：线程池会维持核心数量的线程数。

2、**等待队列的容量**：。

3、**最大线程数**：任务数量超过线程池的最大线程数，任务会进入等待队列，等待空闲的线程。

4、**拒绝策略**：线程池达到容量限制后，怎么处理新的线程请求。

比较重要的：

5、**线程的最大存活时间**：闲置线程到达最长存活时间，会被线程池销毁。

6、**线程池线程名的前缀**。方便定位问题，多个线程池的情况下，快速找到哪个线程池的线程存在异常。

<div align="center">
<img width="600"  alt="线程池的核心配置参数" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-config.png"/></div>

### 线程池的处理流程（重点）

1、判断**核心线程池**是否已满：线程数量是否小于核心线程池数量，没满则创建线程执行任务。

2、判断**等待队列**是否已满，没满则把任务存储在队列。

3、则判断**整个线程池**是否已满，没满则直接创建线程执行任务。

4、**线程池**满了就执行拒绝策略。

<div align="center">
<img width="600" alt="线程池的处理流程" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-process.png"/></div>

### 拒绝策略（重点）

1、AbortPolicy / 抛异常：

> 直接抛出拒绝异常（继承自 RuntimeException），会中断调用者的处理过程，所以除非有明确需求，一般不推荐。

2、CallerRunsPolicy / 调用者自己执行：

> **在调用者线程中（也就是说谁把 r 这个任务甩来的），运行当前被丢弃的任务。**
>
> 只会用调用者所在线程来运行任务，也就是说任务不会进入线程池。
>
> 如果线程池已经被关闭，则直接丢弃该任务。

3、DiscardOldestPolicy / 丢弃最老的：

> **丢弃队列中最老的，然后再次尝试提交新任务。**

4、DiscardPolicy / 丢弃：

> 直接丢弃无法加载的任务。这个代码就很简单了，真的是啥也没做。

### 常用的线程池

1、ThreadPoolExecutor

2、ScheduledThreadPoolExecutor

### 线程池的风险

1、死锁：多线程应用都存在的风险。

2、资源不足：线程池的配置不当，可能导致服务器资源耗尽。

### 多线程的使用场景

1、调用多个外部接口，并且接口数据不存在依赖关系，选择多线程。

2、后台调度需要处理多个任务，选择多线程同时处理。

3、通知外部系统，异步处理，不影响自己的核心业务逻辑。

---

## 提高部分

---

### 线程池创建之后，会立即创建核心线程么

不会

### 核心线程永远不会销毁么

取决于 JDK 版本和线程池的配置：

> JDK 1.6 之前，线程池会尽量保持 corePoolSize 个核心线程。
>
> JDK 1.6 以及之后，如果 allowsCoreThreadTimeOut=true，核心线程也可以被终止。

### 空闲线程过多会有什么问题

占用内存：

1、线程相关的栈（虚拟机栈、本地方法栈），程序计数器。

2、ThreadLocal，业务代码中使用 ThreadLocal 又不清理。

3、局部变量：线程处于阻塞状态，肯定还有栈帧没有出栈；栈帧中有局部变量表，凡是被局部变量表引用的内存都不能回收。

### keepAliveTime=0 会怎么样

JDK 1.8中 ，keepAliveTime=0 表示非核心线程执行完立刻终止补充：

> 默认情况下，keepAliveTime 小于 0，初始化的时候才会报错。
>
> 如果 allowsCoreThreadTimeOut，keepAliveTime 必须大于 0，不然初始化报错。

### shutdown 和 shutdownNow 的区别

1、shutdown：平缓关闭，等待所有已添加到线程池中的任务执行完再关闭。

2、shutdownNow：立刻关闭，停止正在执行的任务，并返回队列中未执行的任务。

### 线程池需不需要关闭

如果是 Server 端不重启就不停止提供服务，是不需要特殊处理的。

### 怎么进行异常处理

在业务代码上加 try-catch 进行异常处理即可：

1、如果是 execute：

> 可以自定义线程池，继承 ThreadPoolExecutor 并复写其 afterExecute(Runnable r, Throwable t)方法。
>
> 或者实现 Thread.UncaughtExceptionHandler 接口，实现 void uncaughtException(Thread t, Throwable e) 方法， 并将该 handler 传递给线程池的 ThreadFactory。

2、注意：afterExecute 和 UncaughtExceptionHandler 都不适用 submit。



---





---

参考链接：

- [10问10答：你真的了解线程池吗？](https://developer.aliyun.com/article/784363) （推荐）
- [线程池的实现原理、优点与风险、以及4种线程池实现](https://mikechen.cc/3199.html)
- [Java 中的 Runnable、Callable 和 Future 的关系](https://www.jianshu.com/p/e74ef77ca097)
- [java 线程池的拒绝策略](https://www.jianshu.com/p/f0506e098c5b)
- [创建线程有几种方式](https://segmentfault.com/a/1190000037589073)
- []()
- []()

---

