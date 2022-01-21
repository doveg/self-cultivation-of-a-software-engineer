## thread-pool / 线程池

---

#### 基础部分

###### 线程池的线程状态

1. 初始化：根据配置创建若干个线程
2. 运行：被调用之后为线程分配执行任务
3. 结束：结束之后被线程池收回重新等待分配任务
4. 等待：
5. 睡眠：
6. 销毁：

###### 线程池的处理流程

1. 初始化：根据配置创建线程池
2. 提交任务
3. 判断线程池线程数，是否小于核心线程池数量，小于则直接创建线程
4. 判断等待队列是否已满，满了就执行拒绝策略，没有则直接创建线程

<div align="center">
<img width="400" alt="线程池的处理流程" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-process.png"/></div>

###### 线程池的核心配置参数

1. 线程池的最大容量：任务数量超过线程池的最大线程数，任务会进入等待队列，等待空闲的线程
2. 核心线程池：线程池会维持核心数量的线程数
3. 存活时间：闲置线程到达最长存活时间，会被线程池销毁
4. 拒绝策略：

<div align="center">
<img width="400"  alt="线程池的核心配置参数" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/java/thread-pool-config.png"/></div>

###### 常用的线程池

1. ThreadPoolExecutor
2. ScheduledThreadPoolExecutor

###### 线程池的风险

1. 死锁：多线程应用都存在的风险
2. 资源不足：线程池的配置不当，可能导致服务器资源耗尽

###### Runnable 和 Thread 的关系

1. Runnable
2. Thread

###### Callable 和 Future 的关系

1. Callable
2. Future

---

#### 提高部分

###### 线程池创建之后，会立即创建核心线程么

    不会

###### 核心线程永远不会销毁么

    取决于 JDK 版本和线程池的配置：
    JDK 1.6 之前，线程池会尽量保持 corePoolSize 个核心线程
    JDK 1.6 以及之后，如果 allowsCoreThreadTimeOut=true，核心线程也可以被终止

###### 空闲线程过多会有什么问题

    占用内存：
    1. 线程相关的栈，程序计数器
    2. ThreadLocal
    3. 局部变量

###### keepAliveTime=0会怎么样

    JDK 1.8中 ，keepAliveTime=0 表示非核心线程执行完立刻终止
    补充：
        默认情况下，keepAliveTime 小于 0，初始化的时候才会报错
        如果 allowsCoreThreadTimeOut，keepAliveTime 必须大于 0，不然初始化报错

###### shutdown 和 shutdownNow 的区别

    shutdown：
        平缓关闭，等待所有已添加到线程池中的任务执行完再关闭
    shutdownNow：
        立刻关闭，停止正在执行的任务，并返回队列中未执行的任务

###### 线程池需不需要关闭

    如果是 Server 端不重启就不停止提供服务，是不需要特殊处理的。

###### 怎么进行异常处理

    在业务代码上加 try-catch 进行异常处理即可

    如果是 execute：
        - 可以自定义线程池，继承 ThreadPoolExecutor 并复写其 afterExecute(Runnable r, Throwable t)方法
        - 或者实现 Thread.UncaughtExceptionHandler 接口，实现 void uncaughtException(Thread t, Throwable e) 方法，
            并将该 handler 传递给线程池的 ThreadFactory
    注意：
        - afterExecute 和 UncaughtExceptionHandler 都不适用 submit

---

参考链接：

- [10问10答：你真的了解线程池吗？](https://developer.aliyun.com/article/784363)
- [线程池的实现原理、优点与风险、以及4种线程池实现](https://mikechen.cc/3199.html)

---
