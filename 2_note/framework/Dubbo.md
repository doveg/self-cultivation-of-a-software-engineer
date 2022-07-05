# Dubbo

---

## 基础部分

---

### Dubbo 通信

默认使用 NIO Netty 框架。

远程通信方案：

    1、Webservice：效率不高基于 soap 协议。项目中不推荐使用。
    2、Restful 形式的服务：http+json。如果服务太多，服务之间调用关系混乱，需要治疗服务。
    3、Dubbo。使用 rpc 协议进行远程调用，直接使用 socket（TCP 长连接)通信。

使用 Dubbo 进行通信

    1）服务提供者；Provider
    2）服务消费者；Consumer
    3）注册中心；Registry
    4）监控中心；Monitor

### Dubbo 的核心功能

Remoting：网络通信框架，提供对多种 NIO 框架抽象封装，包括 “同步转异步” 和 “请求 - 响应” 模式的信息交换方式。

Cluster：服务框架，提供基于接口方法的透明远程过程调用，包括多协议支持，以及软负载均衡，失败容错，地址路由，动态配置等集群支持。

Registry：服务注册，基于注册中心目录服务，使服务消费方能动态的查找服务提供方，使地址透明，使服务提供方可以平滑增加或减少机器。

### Dubbo 注册中心

推荐使用 Zookeeper 作为注册中心，还有 Redis、Multicast、Simple 注册中心，但不推荐。

### Dubbo 服务注册与发现的流程

流程说明：

    Provider（提供者）绑定指定端口并启动服务
    提供者连接注册中心，并将本机 IP、端口、应用信息和提供服务信息发送至注册中心存储
    Consumer（消费者），连接注册中心 ，并发送应用信息、所求服务信息至注册中心
    注册中心根据消费者所求服务信息匹配对应的提供者列表发送至 Consumer 应用缓存
    Consumer 在发起远程调用时基于缓存的消费者列表择其一发起调用
    Provider 状态变更会实时通知注册中心、在由注册中心实时推送至 Consumer

设计的原因：

    Consumer 与 Provider 解偶，双方都可以横向增减节点数。
    注册中心对本身可做对等集群，可动态增减节点，并且任意一台宕掉后，将自动切换到另一台
    去中心化，双方不直接依懒注册中心，即使注册中心全部宕机短时间内也不会影响服务的调用
    服务提供者无状态，任意一台宕掉后，不影响使用

### Dubbo 框架设计分层

一共划分了 10 个层：

    服务接口层（Service）：该层是与实际业务逻辑相关的，根据服务提供方和服务消费方的业务设计对应的接口和实现。
    配置层（Config）：对外配置接口，以 ServiceConfig 和 ReferenceConfig 为中心。
    服务代理层（Proxy）：服务接口透明代理，生成服务的客户端 Stub 和服务器端 Skeleton。
    服务注册层（Registry）：封装服务地址的注册与发现，以服务 URL 为中心。
    集群层（Cluster）：封装多个提供者的路由及负载均衡，并桥接注册中心，以 Invoker 为中心。
    监控层（Monitor）：RPC 调用次数和调用时间监控。
    远程调用层（Protocol）：封将 RPC 调用，以 Invocation 和 Result 为中心，扩展接口为 Protocol、Invoker 和 Exporter。
    信息交换层（Exchange）：封装请求响应模式，同步转异步，以 Request 和 Response 为中心。
    网络传输层（Transport）：抽象 mina 和 netty 为统一接口，以 Message 为中心。
    同步序列化（serialize)

### Dubbo 集群的负载均衡策略

Dubbo 提供了常见的集群策略实现，并预扩展点予以自行实现。

    Random LoadBalance: 随机选取提供者策略，有利于动态调整提供者权重。截面碰撞率高，调用次数越多，分布越均匀；
    RoundRobin LoadBalance: 轮循选取提供者策略，平均分布，但是存在请求累积的问题；，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。
    LeastActive LoadBalance: 最少活跃调用策略，解决慢提供者接收更少的请求；
    ConstantHash LoadBalance: 一致性 Hash 策略，使相同参数请求总是发到同一提供者，一台机器宕机，可以基于虚拟节点，分摊至其他提供者，避免引起提供者的剧烈变动；

### Dubbo 的集群容错方案

    Failover Cluster：失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。（默认）
    Failfast Cluster：快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
    Failsafe Cluster：失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。
    Failback Cluster：失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。
    Forking Cluster：并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。可通过 forks=“2” 来设置最大并行数。
    Broadcast Cluster：广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息。

### Dubbo Monitor 的实现原理

Consumer 端在发起调用之前会先走 fifilter 链；provider 端在接收到请求时也是先走 fifilter 链，然后才进行真正的业务逻辑处理。默认情况下，在 consumer 和
provider 的 fifilter 链中都会 Monitorfifilter。

    MonitorFilter 向 DubboMonitor 发送数据
    DubboMonitor 将数据进行聚合后（默认聚合 1min 中的统计数据）暂存到 ConcurrentMap<Statistics, AtomicReference> statisticsMap，然后使用一个含有 3 个线程（线程名字：DubboMonitorSendTimer）的线程池每隔 1min 钟，调用 SimpleMonitorService 遍历 statisticsMap 中的统计数据，每发送完毕一个，就重置当前的 Statistics 的 AtomicReference
    SimpleMonitorService 将这些聚合数据塞入 BlockingQueue queue 中（队列大写为 100000）
    SimpleMonitorService 使用一个后台线程（线程名为：DubboMonitorAsyncWriteLogThread）将 queue 中的数据写入文件（该线程以死循环的形式来写）
    SimpleMonitorService 还会使用一个含有 1 个线程（线程名字：DubboMonitorTimer）的线程池每隔 5min 钟，将文件中的统计数据画成图表

### Dubbo 支持哪些序列化方式

默认使用 Hessian 序列化，还有 Dubbo、FastJson、Java 自带序列化。

### Dubbo 的安全机制

Dubbo 通过 Token 令牌防止用户绕过注册中心直连，然后在注册中心上管理授权。Dubbo 还提供服务黑白名单，来控制服务所允许的调用方。

### Dubbo 通信协议为什么要消费者比提供者个数多

Dubbo 协议采用单一长连接，假设网络为千兆网卡（1024Mbit=128MByte)，根据测试经验数据每条连接最多只能压满 7MByte（不同的环境可能不一样，供参考)，

理论上 1 个服务提供者需要 20 个服务消费者才能压满网卡。

### Dubbo 通信协议为什么不能传大包

Dubbo 协议采用单一长连接，如果每次请求的数据包大小为 500KByte，

假设网络为千兆网卡（1024Mbit=128MByte)，每条连接最大 7MByte（不同的环境可能不一样，供参考)
，单个服务提供者的 TPS（每秒处理事务数）最大为：128MByte / 500KByte = 262。

单个消费者调用单个服务提供者的 TPS（每秒处理事务数）最大为：7MByte / 500KByte = 14。

如果能接受，可以考虑使用，否则网络将成为瓶颈。

### Dubbo 通信协议为什么采用异步单一长连接

因为服务的现状大都是服务提供者少，通常只有几台机器，而服务的消费者多，可能整个网站都在访问该服务，

比如 Morgan 的提供者只有 6 台提供者，却有上百台消费者，每天有 1.5 亿次调用，如果采用常规的 hessian 服务，服务提供者很容易就被压跨，

**通过单一连接，保证单一消费者不会压死提供者，长连接，减少连接握手验证等，并使用异步 IO，复用线程池，防止 C10K 问题。**

### RMI 协议

RMI 协议采用 JDK 标准的 java.rmi.* 实现，采用阻塞式短连接和 JDK 标准序列化方式，Java 标准的远程调用协议。

    连接个数：多连接
    连接方式：短连接
    传输协议：TCP
    传输方式：同步传输
    序列化：Java 标准二进制序列化
    适用范围：传入传出参数数据包大小混合，消费者与提供者个数差不多，可传文件。
    适用场景：常规远程服务方法调用，与原生 RMI 服务互操作

### Hessian 协议

Hessian 协议用于集成 Hessian 的服务，Hessian 底层采用 Http 通讯，采用 Servlet 暴露服务，Dubbo 缺省内嵌 Jetty 作为服务器实现基于 Hessian
的远程调用协议。

    连接个数：多连接
    连接方式：短连接
    传输协议：HTTP
    传输方式：同步传输
    序列化：Hessian 二进制序列化
    适用范围：传入传出参数数据包较大，提供者比消费者个数多，提供者压力较大，可传文件。
    适用场景：页面传输，文件传输，或与原生 hessian 服务互操作

### Dubbo SPI

SPI 全称为 Service Provider Interface，是一种服务发现机制。

SPI 的本质是将接口实现类的全限定名配置在文件中，并由服务加载器读取配置文件，加载实现类。

这样可以在运行时，动态为接口替换实现类。正因此特性，我们可以很容易的通过 SPI 机制为我们的程序提供拓展功能。

SPI 机制在第三方框架中也有所应用，比如 Dubbo 就是通过 SPI 机制加载所有的组件。

不过，Dubbo 并未使用 Java 原生的 SPI 机制，而是对其进行了增强，使其能够更好的满足需求。

在 Dubbo 中，SPI 是一个非常重要的模块。

基于 SPI，我们可以很容易的对 Dubbo 进行拓展。如果大家想要学习 Dubbo 的源码，SPI 机制务必弄懂。

SPI 主要用于框架中，框架定义好接口，不同的使用者有不同的需求，因此需要有不同的实现，而 SPI 就通过定义一个特定的位置，

Java SPI 约定在 Classpath 下的 META-INF/services/ 目录里创建一个以服务接口命名的文件，然后文件里面记录的是此 jar 包提供的具体实现类的全限定名。

1、对 Dubbo 进行扩展，不需要改动 Dubbo 的源码

2、延迟加载，可以一次只加载自己想要加载的扩展实现。

3、增加了对扩展点 IOC 和 AOP 的支持，一个扩展点可以直接 setter 注入其它扩展点。

4、Dubbo 的扩展机制能很好的支持第三方 IoC 容器，默认支持 Spring Bean。

###

---

## 提高部分

---

###

---


---

参考链接：

- [Dubbo 协议及序列化](https://www.cnblogs.com/jameszheng/p/10271341.html)
- [Dubbo SPI](https://dubbo.apache.org/zh/docs/v2.7/dev/source/dubbo-spi/)
- []()
- []()
- []()

---



