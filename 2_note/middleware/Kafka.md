## Kafka

---

#### 基础部分

###### Kafka 版本命名

- 版本命名：Scala 编译器版本 + Kafka 版本号
- Kafka 服务器端的代码完全由 Scala 语言编写
- Scala 同时支持面向对象编程和函数式编程，用 Scala 写成的源代码编译之后也是普通的 .class 文件

###### Kafka 核心概念

- **Topic / 主题**：
    - 发布订阅的对象


- **Producer / 生产者**：
    - 向主题发布消息的客户端应用程序
    - 通常持续不断地向一个或多个主题发送消息


- **Consumer / 消费者**：
    - 订阅这些主题消息的客户端应用程序
    - 能够同时订阅多个主题的消息，从 Kafka 中拉取消息进行消费


- **Broker / 代理**：
    - 负责接收和处理客户端发送过来的请求，以及对消息进行持久化
    - 一个或多个 Broker 组成了一个 Kafka 集群


- 备份机制：
    - 把相同的数据拷贝到多台机器上
    - 相同的数据拷贝被称为副本
    - 领导者副本：对外提供服务，这里的对外指的是与 客户端程序进行交互
    - 追随者副本：被动地追随领导者副本而已，不能与外界进行交互


- **Partition / 分区**：
    - 是将每个主题划分成多个分区，每个分区是一组有序的消息日志
    - 生产者生产的每条消息只会被发送到一个分区中
    - 每个分区下可以配置若干个副本，其中只能有 1 个领 导者副本和 N-1 个追随者副本
    - 生产者向分区写入消息，每条消息在分区中的位置信息叫位移


- **Consumer Group / 消费者组**：
    - 多个消费者实例共同组成一个组来消费一组主题
    - 这组主题中的每个分区都只会被组内的一个消费者实例消费，其他消费者实例不能消费它
    - 如果所有实例都属于同一个 Group， 那么它实现的就是消息队列模型
    - 如果所有实例分别属于不 同的 Group，那么它实现的就是发布/订阅模型


- Coordinator / 协调者：
    - 专门为 Consumer Group 服务，负责为 Group 执行 Rebalance 以及提供位移管理和组成员管理等
    - 所有 Broker 都有各自的 Coordinator 组件


- Consumer Offset / 消费者位移：
    - 消费者消费进度，每个消费者都有自己的消费者位移


- Rebalance / 重平衡：
    - 消费者组内某个消费者实例挂掉后，其他消费者实例自动重新分配订阅主题分区的过程
    - Rebalance 是 Kafka 消费者端实现高可用的重要手段


- AR（Assigned Replicas）：
    - 分区中的所有副本
    - 所有消息会先发送到 leader 副本，然后 follower 副本才能从 leader 中拉取消息进行同步
    - 但是在同步期间，follower 对于 leader 而言会有一定程度的滞后，这个时候 follower 和 leader 并非完全同步状态


- OSR（Out Sync Replicas）：
    - follower 副本与 leader 副本没有完全同步或滞后的副本集合


- ISR（In Sync Replicas）：
    - 与 leader 保持完全同步的副本
    - 如果某个在 ISR 中的 follower 副本落后于 leader 副本太多，则会被从 ISR 中移除
    - 否则如果完全同步，会从 OSR 中移至 ISR 集合


- HW（High Watermark） / 高水位：
    - 它标识了一个特定的消息偏移量（offset）
    - 消费者只能拉取到这个水位 offset 之前的消息


- LEO（Log End Offset）：
    - 标识当前日志文件中下一条待写入的消息的 offset

###### Kafka 特性

- 消息持久化：
    - Kafka 会把消息持久化到本地文件系统中，并且具有极高的性能


- **批量发送**：
    - Kafka 支持以消息集合为单位进行批量发送，以提高效率


- **Push-and-Pull**：
    - Kafka 中的 Producer 和 Consumer 采用的是 Push-and-Pull 模式
    - Producer 向 Broker Push 消息，Consumer 从 Broker Pull 消息


- **分区机制（Partition）**：
    - Kafka 的 Broker 端支持消息分区，Producer 可以决定把消息发到哪个 Partition
    - 在一个 Partition 中消息的顺序就是 Producer 发送消息的顺序
    - 一个 Topic 中的 Partition 数是可配置的，Partition 是 Kafka 高吞吐量的重要保证

###### Kafka 系统架构图

<div align="center">
<img width="600"  alt="Kafka 系统架构图" src="https://github.com/bourneo/self-cultivation-of-a-software-engineer/blob/master/7_image/middleware/Kafka系统架构图.webp"/></div>

###### Kafka 生产者分区策略

- Kafka 的消息组织方式实际上是三级结构：主题、分区、消息，主题下的每条消息只会保存在某一个分区中


- **轮询策略**：有非常优秀的负载均衡表现，总是能保证消息最大限度地被平均分配到所有分区上
    - 故默认情况下它是最合理的分区策略，也是我们最常用的分区策略之一


- **随机策略**
    - 本质上看随机策略也是力求将数据均匀地打散到各个分区，但从实际表现来看，它要逊于轮询策略
    - 如果追求数据的均匀分布，还是使用轮询策略比较好


- **按消息键保序策略**
    - Kafka 允许为每条消息定义消息键，简称为 Key


- 地理位置的分区策略
    - 可以从所有分区中找出那些 Leader 副本在某个地区的所有分区，然后随机挑选一个进行消息发送

###### Kafka 消息压缩

基本过程：Producer 端压缩、Broker 端保存、Consumer 端解压缩

###### Kafka 消费者策略

- Round：轮循


- Range：对一个消费者组来说，消费方式是分区总数除以消费者总数来决定，一般如果不能整除，往往是从头开始将剩余的分区分配开


- Sticky：
    - 在 Range 上的一种升华，且前面两个当同组内有新的消费者加入或者旧的消费者退出的时候，会从新开始决定消费者消费方式
    - 但是 Sticky 在同组中有新的新的消费者加入或者旧的消费者退出时，不会直接开始新的 Range 分配
    - 而是保留现有消费者原来的消费策略，将退出的消费者所消费的分区平均分配给现有消费者
    - 新增消费者同理，同其他现存消费者的消费策略中分离

###### Kafka 消息丢失问题-生产者丢失数据

原因：

- 网络抖动，导致消息压根就没有发送到 Broker 端
- 消息本身不合格导致 Broker 拒绝接收（比如消息太大了，超过了 Broker 的承受能力）等

解决方案：

- **Producer 永远要使用带有回调通知的发送 API**
- 也就是说不要使用 producer.send(msg)，而要使用 producer.send(msg, callback)
- 它能准确地告诉你消息是否真的提交成功了，一旦出现消息提交失败的情况，你就可以有针对性地进行处理

###### Kafka 消息丢失问题-消费者丢失数据

原因：

- 假如其中某个线程运行失败了，它负责的消息没有被成功处理，但位移已经被更新了，因此这条消息对于 Consumer 而言实际上是丢失了

解决方案：

- **如果是多线程异步处理消费消息，Consumer 程序不要开启自动提交位移，而是要应用程序手动提交位移**

###### Kafka 重复消费问题

重复消费的场景：

- **Consumer 在消费过程中，应用进程被强制 kill 掉或发生异常退出**
    - 解决方案：
        - 在发生异常时，处理好未提交的 offset


- 消费者消费时间过长
    - 解决方案：
        - 提高消费能力，提高单条消息的处理速度； 根据实际场景可讲 max.poll.interval.ms 值设置大一点，避免不必要的 rebalance；可适当减小
          max.poll.records 的值，默认值是 500，可根据实际消息速率适当调小
        - 生成消息时，可加入唯一标识符如消息 id，在消费端，保存最近的 1000 条消息 id 存入到 redis 或 mysql 中，消费的消息时通过前置去重

###### Kafka 消息消费顺序问题

kafka 的 topic 是无序的，但是一个 topic 包含多个 partition，每个 partition 内部是有序的

如何保证消费顺序：

1. 可以设置 topic，有且只有一个 partition
2. 根据业务需要，需要顺序的 指定为同一个 partition
3. 根据业务需要，比如同一个订单，使用同一个 key，可以保证分配到同一个 partition 上

###### Kafka 如何实现高性能

- 顺序读写
    - kafka 的消息是不断追加到文件中的，这个特性使 kafka 可以充分利用磁盘的顺序读写性能


- **零拷贝**
    - Sendfile：Kafka 依赖 Linux 内核提供的 Sendfile 系统调用
    - MMAP 技术：Memory Mapped Files 对文件的操作不是 write/read，而是直接对内存地址的操作


- **批量发送读取**
    - Kafka 的批量包括批量写入、批量发布等。它在消息投递时会将消息缓存起来，然后批量发送
    - 消费端在消费消息时，是批量进行拉取，提高了消息的处理速度


- **数据压缩**
    - 减少传输的数据量，减轻对网络传输的压力
    - Consumer 需进行解压，虽然增加了 CPU 的工作，但在对大数据处理上，瓶颈在网络上而不是 CPU


- 分区机制
    - kafka 中的 topic 中的内容可以被分为多 partition 存在，每个 partition 又分为多个段 segment
    - 所以每次操作都是针对一小部分做操作，很轻便，并且增加并行操作的能力

###### Kafka 如何保证高可用

- 备份机制：Kafka 会尽量将所有的 Partition 以及各 Partition 的副本均匀地分配到整个集群的各个 Broker 上
- 故障恢复机制：首先需要在集群所有 Broker 中选出一个 Controller，负责各 Partition 的 Leader 选举以及 Replica 的重新分配

---

#### 提高部分

######

---





---

参考链接：

- [Kafka核心知识总结](https://mp.weixin.qq.com/s?__biz=MzUyOTg1OTkyMA==&mid=2247487063&idx=1&sn=d7c65359630a67695a079d62fce989c0)

---






