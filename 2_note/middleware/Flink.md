# Flink

---

## 基础部分

---

### Flink

一个以流为核心的高可用、高性能的分布式计算引擎。

具备流批一体，高吞吐、低延迟，容错能力，大规模复杂计算等特点，在数据流上提供 数据分发、通信等功能。

### 基本概念

**数据流：**

所有产生的 数据 都天然带有 时间概念，把 事件 按照时间顺序排列起来，就形成了一个事件流，也被称作数据流。

**有界数据：**

在一个确定的时间范围内的数据流，有开始，有结束，一旦确定就不会再改变，一般 批处理 用来处理有界数据，如上图的 bounded stream。

**无界数据：**

持续产生的数据流，数据是无限的，有开始，无结束，一般 流处理 用来处理无界数据。

**流批一体：**

Flink 的设计思想是以 流 为核心，批是流的特例，擅长处理 无界 和 有界 数据，

Flink 提供 精确的时间控制能力 和 有状态 计算机制，可以轻松应对无界数据流，同时 提供 窗口 处理有界数据流。

所以被成为流批一体。

**容错能力：**

在分布式系统中，硬件故障、进程异常、应用异常、网络故障等异常无处不在，

Flink 引擎必须保证故障发生后 不仅可以 重启 应用程序，还要 确保 其内部状态保持一致，从最后一次正确的时间点重新出发

**集群级容错：**

Flink 与 集群管理器紧密连接，如 YARN、Kubernetes，当进程挂掉后，自动重启新进程接管之前的工作。同时具备 高可用性 ，可消除所有单点故障，

**应用级容错：**

Flink 使用 轻量级分布式快照，设计检查点（checkpoint）实现可靠容错。

### Flink 常用的算子有哪些？

分两部分：

（1）数据读取，这是 Flink 流计算应用的起点， 常用算子有：

    从内存读：fromElements、
    从文件读：readTextFile、
    Socket 接入 ：socketTextStream、
    自定义读取：createInput

（2）处理数据的算子，主要用于 转换 过程，常用的算子包括：

    Map（单输入单输出）、
    FlatMap（单输入、多输出）、
    Filter（过滤）、
    KeyBy（分组）、
    Reduce（聚合）、
    Window（窗口）、
    Connect（连接）、
    Split（分割）等。

### Flink 和 Spark Streaming 的区别

Flink 和 Spark Sreaming 最大的区别在于：Flink 是标准的实时处理引擎，基于事件驱动，以流为核心，

Spark Streaming 的 RDD 实际是一组小批次的 RDD 集合，是微批（Micro-Batch）的模型，以批为核心。

### 怎么做压力测试和监控

一，产生数据流的速度如果过快，而下游的算子消费不过来的话，会产生背压。

背压的监控可以使用 Flink Web UI(localhost:8081) 来可视化监控，一旦报警就能知道。

一般情况下背压问题的产生可能是由于 sink 这个 操作符没有优化好，做一下 优化就可以了。

比如如果是写入 ElasticSearch， 那么可以改成批量写入，可以调 大 ElasticSearch 队列的大小等等策略。

二，设置 watermark 的最大延迟时间这个参数，如果设置的过大，可能会造成内存的压力。

可以设置最大延迟时间小一些，然后把迟到元素发送到侧输出流中去。 晚一点更新结果。

或者使用类似于 RocksDB 这样的状态后端， RocksDB 会开辟堆外存储空间，但 IO 速度会变慢，需要权衡。

三，还有就是滑动窗口的长度如果过长，而滑动距离很短的话，Flink 的性能会下降的很厉害。

我们主要通过时间分片 的方法，将每个元素只存入一个 “重叠窗 口”，这样就可以减少窗口处理中状态的写入。（详情链接：Flink 滑动窗口优化）

四，状态后端使用 RocksDB，还没有碰到被撑爆的问题

### Flink 组件栈

自下而上，每一层分别代表：

**Deploy 层：**

该层主要涉及了 Flink 的部署模式，在上图中我们可以看出，Flink 支持包括 local、Standalone、Cluster、Cloud 等多种部署模式。

**Runtime 层：**

Runtime 层提供了支持 Flink 计算的核心实现，比如：支持分布式 Stream 处理、JobGraph 到 ExecutionGraph 的映射、调度等等，为上层 API 层提供基础服务。

**API 层：**

API 层主要实现了面向流（Stream）处理和批（Batch）处理 API，其中面向流处理对应 DataStream API，面向批处理对应 DataSet API，后续版本，Flink 有计划将
DataStream 和 DataSet API 进行统一。

**Libraries 层：**

该层称为 Flink 应用框架层，根据 API 层的划分，在 API 层之上构建的满足特定应用的实现计算框架，也分别对应于面向流处理和面向批处理两类。

面向流处理支持：CEP（复杂事件处理）、基于 SQL-like 的操作（基于 Table 的关系操作）；

面向批处理支持：FlinkML（机器学习库）、Gelly（图处理）。

### Flink 集群的角色

TaskManager，JobManager，Client 三种角色。

**JobManager：**

扮演着集群中的管理者 Master 的角色，它是整个集群的协调者，负责接收 Flink Job，协调检查点，Failover 故障恢复等，同时管理 Flink 集群中从节点 TaskManager。

**TaskManager：**

实际负责执行计算的 Worker，在其上执行 Flink Job 的一组 Task，每个 TaskManager 负责管理其所在节点上的资源信息，如内存、磁盘、网络，在启动的时候将资源的状态向
JobManager 汇报。

**Client：**

Flink 程序提交的客户端，当用户提交一个 Flink 程序时，会首先创建一个 Client，该 Client 首先会对用户提交的 Flink 程序进行预处理，并提交到 Flink 集群中处理，

所以 Client 需要从用户提交的 Flink 程序配置中获取 JobManager 的地址，并建立到 JobManager 的连接，将 Flink Job 提交给 JobManager。

### Flink 重启策略

Flink 实现了多种重启策略。

    固定延迟重启策略（Fixed Delay Restart Strategy）。
    故障率重启策略（Failure Rate Restart Strategy）。
    没有重启策略（No Restart Strategy）。
    Fallback 重启策略（Fallback Restart Strategy）。

### Flink 中分布式缓存

Flink 实现的分布式缓存和 Hadoop 有异曲同工之妙。目的是在本地读取文件，并把他放在 taskmanager 节点中，防止 task 重复拉取。

### Flink 中广播变量

Flink 是并行的，计算过程可能不在一个 Slot 中进行，那么有一种情况即：当我们需要访问同一份数据。那么 Flink 中的广播变量就是为了解决这种情况。

我们可以把广播变量理解为是一个公共的共享变量，我们可以把一个 dataset 数据集广播出去，然后不同的 task 在节点上都能够获取到，这个数据在每个节点上只会存在一份。

### Flink 中水印

Watermark 是 Apache Flink 为了处理 EventTime 窗口计算提出的一种机制，本质上是一种时间戳。一般来讲 Watermark 经常和 Window 一起被用来处理乱序事件。

通过 watermark 机制来处理 out-of-order 的问题，属于第一层防护，属于全局性的防护，通常说的乱序问题的解决办法，就是指这类；

通过窗口上的 allowedLateness 机制来处理 out-of-order 的问题，属于第二层防护，属于特定 window operator 的防护，late element
的问题就是指这类。

### Flink 是如何支持批流一体的？

Flink 的开发者认为批处理是流处理的一种特殊情况。批处理是有限的流处理。Flink 使用一个引擎支持了 DataSet API 和 DataStream API。

---

## 核心部分

---

### Flink 的四大基石包含哪些？

Flink 四大基石分别是：Checkpoint（检查点）、State（状态）、Time（时间）、Window（窗口）。

### 说说 Flink 窗口，以及划分机制

窗口概念：将无界流的数据，按时间区间，划分成多份数据，分别进行统计 (聚合)

Flink 支持两种划分窗口的方式 (time 和 count)，第一种，按时间驱动进行划分、另一种按数据驱动进行划分。

### 讲一下 Flink 的 Time 概念

在 Flink 的流式处理中，会涉及到时间的不同概念，主要分为三种时间机制，

EventTime[事件时间]

> 事件发生的时间，例如：点击网站上的某个链接的时间，每一条日志都会记录自己的生成时间。
> 如果以 EventTime 为基准来定义时间窗口那将形成 EventTimeWindow, 要求消息本身就应该携带 EventTime

IngestionTime[摄入时间]

> 数据进入 Flink 的时间，如某个 Flink 节点的 sourceoperator 接收到数据的时间，例如：某个 source 消费到 kafka 中的数据
> 如果以 IngesingtTime 为基准来定义时间窗口那将形成 IngestingTimeWindow, 以 source 的 systemTime 为准

ProcessingTime[处理时间]

> 某个 Flink 节点执行某个 operation 的时间，例如：timeWindow 处理数据时的系统时间，默认的时间属性就是 Processing Time
> 如果以 ProcessingTime 基准来定义时间窗口那将形成 ProcessingTimeWindow，以 operator 的 systemTime 为准

在 Flink 的流式处理中，绝大部分的业务都会使用 EventTime，一般只在 EventTime 无法使用时，才会被迫使用 ProcessingTime 或者 IngestionTime。

###

###

###

###

###

###

###

###

###

###

---

## 提高部分

---

###

###

###

###

###

---








---

参考链接：

- [Flink 面试大全总结](https://mp.weixin.qq.com/s?__biz=Mzg5NDY3NzIwMA==&mid=2247497240&idx=1&sn=954c0702a2d842f9facb4e36c8c44563)
- []()
- []()
- []()
- []()
- []()

---















