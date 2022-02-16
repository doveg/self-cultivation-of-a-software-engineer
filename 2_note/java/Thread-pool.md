## Thread-pool / 线程池

---

#### 基础部分

###### 线程的状态

1. 初始 (NEW)：线程被构建
2. 运行 (RUNNABLE)：操作系统中的就绪和运行两种状态
3. 阻塞 (BLOCKED)：线程阻塞于锁
4. 等待 (WAITING)：表示当前线程需要其他线程通知
5. 定时等待 (TIMED_WAITING)：指定等待时间自己返回
6. 终止 (TERMINATED)：当前线程已经执行完毕

###### 线程池的处理流程

1. 判断线程池线程数，是否小于核心线程池数量，小于则直接创建线程
2. 判断等待队列是否已满，满了就执行拒绝策略，没有则直接创建线程

<div align="center">
<img width="600" alt="线程池的处理流程" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-process.png"/></div>

###### 线程池的核心配置参数

1. 线程池的最大容量：任务数量超过线程池的最大线程数，任务会进入等待队列，等待空闲的线程
2. 核心线程池：线程池会维持核心数量的线程数
3. 存活时间：闲置线程到达最长存活时间，会被线程池销毁
4. 拒绝策略

<div align="center">
<img width="600"  alt="线程池的核心配置参数" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-config.png"/></div>

###### 常用的线程池

1. ThreadPoolExecutor
2. ScheduledThreadPoolExecutor

###### 线程池的风险

1. 死锁：多线程应用都存在的风险
2. 资源不足：线程池的配置不当，可能导致服务器资源耗尽

###### 线程的创建的两种方式

1. 通过继承 Thread 类，重写 Thread 的 run () 方法，将线程运行的逻辑放在其中


2. 通过实现 Runnable 接口，实例化 Thread 类

###### Runnable 和 Thread 的关系

1. Thread 类是 Runnable 接口的实现类，继承 Thread 类时覆写的还是 Runnable 接口的 run () 方法


2. 使用 Runnable 是最方便的，因为其可以避免单继承的局限，同时也可以更好进行功能的扩充

###### Runnable、Callable 和 Future 的关系

1. Runnable：
    - Runnable.run () 方法没有返回值
    - 不会向外抛异常


2. Callable：
    - Callable.call () 方法有返回值
    - 可以向外抛异常


3. Future：
    - 因为 Thread 不接受 Callable，只接受 Runnable，所以有了 FutureTask
    - FutureTask 是 Runnable 和 Future 的实现类：FutureTask 继承了 RunnableFuture，RunnableFuture 实现了 Runnable
      和 Future 接口

---

#### 提高部分

###### 线程池创建之后，会立即创建核心线程么

不会

###### 核心线程永远不会销毁么

取决于 JDK 版本和线程池的配置：

- JDK 1.6 之前，线程池会尽量保持 corePoolSize 个核心线程


- JDK 1.6 以及之后，如果 allowsCoreThreadTimeOut=true，核心线程也可以被终止

###### 空闲线程过多会有什么问题

占用内存：

1. 线程相关的栈（虚拟机栈、本地方法栈），程序计数器
2. ThreadLocal，业务代码中使用 ThreadLocal 又不清理
3. 局部变量：线程处于阻塞状态，肯定还有栈帧没有出栈；栈帧中有局部变量表，凡是被局部变量表引用的内存都不能回收

###### keepAliveTime=0会怎么样

JDK 1.8中 ，keepAliveTime=0 表示非核心线程执行完立刻终止 补充：

- 默认情况下，keepAliveTime 小于 0，初始化的时候才会报错
- 如果 allowsCoreThreadTimeOut，keepAliveTime 必须大于 0，不然初始化报错

###### shutdown 和 shutdownNow 的区别

1. shutdown：平缓关闭；等待所有已添加到线程池中的任务执行完再关闭


2. shutdownNow：立刻关闭；停止正在执行的任务，并返回队列中未执行的任务

###### 线程池需不需要关闭

如果是 Server 端不重启就不停止提供服务，是不需要特殊处理的

###### 怎么进行异常处理

在业务代码上加 try-catch 进行异常处理即可

1. 如果是 execute：
    - 可以自定义线程池，继承 ThreadPoolExecutor 并复写其 afterExecute(Runnable r, Throwable t)方法
    - 或者实现 Thread.UncaughtExceptionHandler 接口，实现 void uncaughtException(Thread t, Throwable e) 方法，
      并将该 handler 传递给线程池的 ThreadFactory


2. 注意：afterExecute 和 UncaughtExceptionHandler 都不适用 submit

---





---

参考链接：

- [10问10答：你真的了解线程池吗？](https://developer.aliyun.com/article/784363)
- [线程池的实现原理、优点与风险、以及4种线程池实现](https://mikechen.cc/3199.html)
- [Java 中的 Runnable、Callable 和 Future 的关系](https://www.jianshu.com/p/e74ef77ca097)

---

