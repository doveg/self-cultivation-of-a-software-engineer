# RocketMQ

---

## 基础部分

---

### RocketMQ 核心概念

**NameServer / 命名服务器（注册中心）**：

- 主要负责对于源数据的管理，包括了对于 Topic 和路由信息的管理。

**Producer / 消息生产者**：

- 负责产生消息，一般由业务系统负责产生消息。

**Broker / 经纪人 / 代理服务器（消息中转角色）**：

- 负责存储消息，转发消息。

**Consumer / 消息消费者**：

- 负责消费消息，一般是后台系统负责异步消费。

**Message / 消息**：

- 要传输的信息。

**Topic / 主题**：

- 可以看做消息的规类，它是消息的第一级类型。

**Tag / 标签**：

- Tag 可以看作子主题，它是消息的第二级类型，用于为用户提供额外的灵活性。

**Key / 键**：

- 除了标签，RocketMQ 的消息还可以带上 KEY，可以有多个 KEY。

**Group / 分组**：

- 一个组可以订阅多个 Topic。

**Queue / 队列**：

- 在 Kafka 中叫 Partition，每个 Queue 内部是有序的
- 在 RocketMQ 中分为读和写两种队列，一般来说读写队列数量一致，如果不一致就会出现很多问题。

**Message Queue / 消息队列**：

- 主题被划分为一个或多个子主题，即消息队列。

**Offset / 下标**：

- 也可以认为 Message Queue 是一个长度无限的数组，Offset 就是下标。
- 在 RocketMQ 中，所有消息队列都是持久化，长度无限的数据结构
- 所谓长度无限是指队列中的每个存储单元都是定长，访问其中的存储单元使用 Offset 来访问，Offset 为 java long 类型，64 位，理论上在 100
  年内不会溢出，所以认为是长度无限。

Message Order / 消息顺序：

- 有两种：Orderly（顺序消费）和 Concurrently（并行消费）。

### RocketMQ 的特性

1. 消息的订阅与发布：消息队列的基本功能。
2. **顺序消息**：
    - 消费的顺序与发送的顺序一致，包括全局有序和分区有序，全局有序的 topic 只有一个消息队列，
    - 适用场景：性能要求不高，所有消息需要严格有序。
    - 分区有序的所有消息根据 sharding key 进行分区。同一个分区内的消息按照 FIFO 顺序进行发布和消费。
    - Sharding key 是消息中用来区分不同分区的关键字段，和普通消息的 Key 是完全不同的概念。
    - 适用场景：性能要求高，对于某一类消息需要有序，同时有一个 sharding key 作为分区字段。
3. 定时消息：消息发送到 broker 后，不会立即被消费，等待特定时间投递给真正的 topic。
4. **事务消息**：应用本地事务和发送消息操作可以被定义到全局事务中，要么同时成功，要么同时失败。通过事务消息能达到分布式事务的最终一致。
5. 消息重试：消费者消费失败时，RocketMQ 提供重试机制使得消息可以被再次消费。
6. 流量控制：当 Broker 处理消息的能力达到瓶颈时，通过拒绝生产者的发送请求进行流量控制，当消费者的消息能力达到瓶颈时，通过降低消息的拉取频率进行流量控制。
7. 死信队列：当一条消息达到最大重试次数后，若消费依然失败，则表明消费者在正常情况下无法正确地消费该消息，此时，消息队列不会立刻将消息丢弃，而是将其发送到该消费者对应的特殊队列中。

### RocketMQ 的应用场景

1. 异步：不需要同步等待。
2. 解耦：应用之间不互相依赖。
3. 削峰：避免流量激增导致系统性能问题。

### RocketMQ 的数据存储

RocketMQ 的混合型存储结构针对 Producer 和 Consumer 分别采用了数据和索引部分相分离的存储结构，Producer 发送消息至 Broker 端，然后 Broker
端使用同步或者异步的方式对消息刷盘持久化，保存至 CommitLog 中。

只要消息被刷盘持久化至磁盘文件 CommitLog 中，那么 Producer 发送的消息就不会丢失。

正因为如此，Consumer 也就肯定有机会去消费这条消息，至于消费的时间可以稍微滞后一些也没有太大的关系。

退一步地讲，即使 Consumer 端第一次没法拉取到待消费的消息，Broker 服务端也能够通过长轮询机制等待一定时间延迟后再次发起拉取消息的请求。

具体做法是，使用 Broker 端的后台服务线程 — ReputMessageService 不停地分发请求并异步构建 ConsumeQueue（逻辑消费队列）和
IndexFile（索引文件）数据。然后，Consumer 即可根据 ConsumerQueue 来查找待消费的消息了。

其中，ConsumeQueue（逻辑消费队列）作为消费消息的索引，保存了指定 Topic 下的队列消息在 CommitLog 中的起始物理偏移量 offset，消息大小 size 和消息 Tag 的
HashCode 值。

而 IndexFile（索引文件）则只是为了消息查询提供了一种通过 key 或时间区间来查询消息的方法（ps：这种通过 IndexFile 来查找消息的方法不影响发送与消费消息的主流程）

### RocketMQ 和 Kafka 的数据存储

RocketMQ 采用的是混合型的存储结构，即为 Broker 单个实例下所有的队列共用一个日志数据文件（即为 CommitLog）来存储。

RocketMQ 采用混合型存储结构的缺点在于，会存在较多的随机读操作，因此读的效率偏低。

同时消费消息需要依赖 ConsumeQueue，构建该逻辑消费队列需要一定开销。

Kafka 采用的是独立型的存储结构，每个队列一个文件。

### RocketMQ 的消息过滤

Broker 端消息过滤：

- 在 Broker 中，按照 Consumer 的要求做过滤，优点是减少了对于 Consumer 无用消息的网络传输。
- 缺点是增加了 Broker 的负担，实现相对复杂。

Consumer 端消息过滤：

- 这种过滤方式可由应用完全自定义实现，但是缺点是很多无用的消息要传输到 Consumer 端。

### RocketMQ 的定时消息

定时消息是指消息发到 Broker 后，不能立刻被 Consumer 消费，要到特定的时间点或者等待特定的时间后才能被消费。

如果要支持任意的时间精度，在 Broker 层面，必须要做消息排序，如果再涉及到持久化，那么消息排序要不可避免的产生巨大性能开销。

RocketMQ 支持定时消息，但是不支持任意时间精度，支持特定的 level，例如定时 5s，10s，1m 等。

### RocketMQ 的高可用机制

RocketMQ 是天生支持分布式的，可以配置主从以及水平扩展。

- Master 角色的 Broker 支持读和写，Slave 角色的 Broker 仅支持读，也就是 Producer 只能和 Master 角色的 Broker 连接写入消息；Consumer
  可以连接 Master 角色的 Broker，也可以连接 Slave 角色的 Broker 来读取消息。

消息消费的高可用（主从）：

- 在 Consumer 的配置文件中，并不需要设置是从 Master 读还是从 Slave 读，当 Master 不可用或者繁忙的时候，Consumer 会被自动切换到从 Slave
  读。有了自动切换 Consumer 这种机制，当一个 Master 角色的机器出现故障后，Consumer 仍然可以从 Slave 读取消息，不影响 Consumer
  程序。这就达到了消费端的高可用性。RocketMQ 目前还不支持把 Slave 自动转成 Master，如果机器资源不足，需要把 Slave 转成 Master，则要手动停止 Slave 角色的
  Broker，更改配置文件，用新的配置文件启动 Broker。

消息发送高可用（配置多个主节点）：

- 创建 Topic 的时候，把 Topic 的多个 Message Queue 创建在多个 Broker 组上（相同 Broker 名称，不同 brokerId 的机器组成一个 Broker
  组），这样当一个 Broker 组的 Master 不可用后，其他组的 Master 仍然可用，Producer 仍然可以发送消息。

主从复制：

如果一个 Broker 组有 Master 和 Slave，消息需要从 Master 复制到 Slave 上，有同步和异步两种复制方式。

- 同步复制：同步复制方式是等 Master 和 Slave 均写成功后才反馈给客户端写成功状态。如果 Master 出故障， Slave
  上有全部的备份数据，容易恢复同步复制会增大数据写入延迟，降低系统吞吐量。
- 异步复制：异步复制方式是只要 Master 写成功 即可反馈给客户端写成功状态。在异步复制方式下，系统拥有较低的延迟和较高的吞吐量，但是如果 Master 出了故障，有些数据因为没有被写 入
  Slave，有可能会丢失

通常情况下，应该把 Master 和 Save 配置成同步刷盘方式，主从之间配置成异步的复制方式，这样即使有一台机器出故障，仍然能保证数据不丢，是个不错的选择。

### RocketMQ 负载均衡

Producer 负载均衡：

- Producer 端，每个实例在发消息的时候，默认会轮询所有的 Message Queue 发送，以达到让消息平均落在不同的 Queue 上。而由于 Queue 可以散落在不同的
  Broker，所以消息就发送到不同的 Broker 下

Consumer 负载均衡：

- 如果 Consumer 实例的数量比 Message Queue 的总数量还多的话，多出来的 Consumer 实例将无法分到
  Queue，也就无法消费到消息，也就无法起到分摊负载的作用了。所以需要控制让 Queue 的总数量大于等于 Consumer 的数量。

  消费者的集群模式：启动多个消费者就可以保证消费者的负载均衡（均摊队列） 默认使用的是均摊队列：会按照 Queue 的数量和实例的数量平均分配 Queue
  给每个实例，这样每个消费者可以均摊消费的队列，如下图所示 6 个队列和三个生产者。

  另外一种平均的算法环状轮流分 Queue 的形式，每个消费者，均摊不同主节点的一个消息队列，如下图所示：

- 对于广播模式并不是负载均衡的，要求一条消息需要投递到一个消费组下面所有的消费者实例，所以也就没有消息被分摊消费的说法。

### RocketMQ 死信队列

视频讲解：[两天玩转 RocketMQ-16-死信队列](https://www.bilibili.com/video/BV1uQ4y1d7uS?p=16)

可以监听死信队列，根据消息的重要性，进行重新处理。

当一条消息消费失败，RocketMQ 就会自动进行消息重试，如果消息超过最大重试次数，RocketMQ 就会认为这个消息有问题。这种正常情况下无法被消费的消息称为死信消息（Dead-Letter
Message）。

RocketMQ 不会立刻将这个有问题的消息丢弃，而会将其发送到这个消费者组对应的一种特殊队列：死信队列。死信队列的名称是 Dead-Letter Queue。

死信队列具有以下特性：

1. 一个死信队列对应一个 Group ID， 而不是对应单个消费者实例。
2. 如果一个 Group ID 未产生死信消息，消息队列 RocketMQ 不会为其创建相应的死信队列。
3. 一个死信队列包含了对应 Group ID 产生的所有死信消息，不论该消息属于哪个 Topic。

### RocketMQ 消息重试

视频讲解：[两天玩转 RocketMQ-15-消息重试](https://www.bilibili.com/video/BV1iq4y1E7tP?p=15)

有序消息重试；

无序消息重试。

### RocketMQ 的高速读写

视频讲解：[两天玩转 RocketMQ-15-消息重试](https://www.bilibili.com/video/BV1iq4y1E7tP?p=15)

写入方式：

**随机写**：数据写入不连续的磁盘空间，性能较差。

**顺序写**：连续的磁盘空间，速度较快。

读取过程：

硬盘数据、**内核态**、**用户态**、网络驱动内核、网卡、内存数据。

RocketMQ 跳过用户态，由传统的 4 次复制，简化成 3 次复制，减少了 1 次复制过程。

Java 利用 MappedByteBuffer 类，在 Linux 实现零拷贝。

### RocketMQ 的刷盘机制

视频讲解：[两天玩转 RocketMQ-11-刷盘机制](https://www.bilibili.com/video/BV1uQ4y1d7uS?p=11)

同步刷盘：安全性高、效率低、速度慢。

异步刷盘：安全性低、效率高、速度快。


---

## 提高部分

---

### RocketMQ 工作原理

视频讲解：[动画讲解 - rocketMq 的核心原理](https://www.bilibili.com/video/BV1wU4y1A7GB)

视频讲解：[两天玩转 RocketMQ-04-工作原理](https://www.bilibili.com/video/BV1iq4y1E7tP?p=4)

生产者；

消费者；

经纪人；

命名服务器；

### RocketMQ 消息消费的可靠性

### RocketMQ 消息丢失的解决办法

### RocketMQ 消息消费的流程

Producer 与 NameServer 集群中的其中一个节点（随机选择）建立长连接，定期从 NameServer 获取 Topic 路由信息，并向提供 Topic 服务的 Broker
Master 建立长连接，且定时向 Broker 发送心跳。

Producer 只能将消息发送到 Broker master，但是 Consumer 则不一样，它同时和提供 Topic 服务的 Master 和 Slave 建立长连接，既可以从 Broker
Master 订阅消息，也可以从 Broker Slave 订阅消息。

### RocketMQ 的消息堆积问题

消息中间件的主要功能是异步解耦，还有个重要功能是挡住前端的数据洪峰，保证后端系统的稳定性，这就要求消息中间件具有一定的消息堆积能力。

消息堆积分以下两种情况：

- 消息堆积在内存 Buffer，一旦超过内存 Buffer，可以根据一定的丢弃策略来丢弃消息，如 CORBA Notification 规范中描述。
    - 适合能容忍丢弃消息的业务，这种情况消息的堆积能力主要在于内存 Buffer 大小，而且消息堆积后，性能下降不会太大，因为内存中数据多少对于对外提供的访问能力影响有限。
- 消息堆积到持久化存储系统中，例如 DB，KV 存储，文件记录形式。
    - 当消息不能在内存 Cache 命中时，要不可避免的访问磁盘，会产生大量读 IO，读 IO 的吞吐量直接决定了消息堆积后的访问能力。

评估消息堆积能力主要有以下四点：

1. 消息能堆积多少条，多少字节？即消息的堆积容量。
2. 消息堆积后，发消息的吞吐量大小，是否会受堆积影响？
3. 消息堆积后，正常消费的 Consumer 是否会受影响？
4. 消息堆积后，访问堆积在磁盘的消息时，吞吐量有多大？

### RocketMQ 的顺序消息

一个 topic 下有多个队列，为了保证发送有序， RocketMQ 提供了 MessageQueueSelector 队列选择机制，他有三种实现:

- SelectMessageQueueByHash
- SelectMessageQueueByMachineRoom
- SelectMessageQueueByRandom

我们可使用 Hash 取模法，让同一个订单发送到同一个队列中，再使用同步发送，只有同个订单的创建消息发送成功，再发送支付消息。这样，我们保证了发送有序。

RocketMQ 的 topic 内的队列机制，可以保证存储满足 FIFO（First Input First Output 简单说就是指先进先出）, 剩下的只需要消费者顺序消费即可。

**RocketMQ 仅保证顺序发送，顺序消费由消费者业务保证**。

### RocketMQ 的消息优先级（Message Priority）

优先级是指在一个消息队列中，每条消息都有不同的优先级，一般用整数来描述，优先级高的消息先投递，如果消息完全在一个内存队列中，那么在投递前可以按照优先级排序，令优先级高的先投递。

具体到 RocketMQ，由于 RocketMQ 所有消息都是持久化的，所以如果按照优先级来排序，资源开销会非常大，

**因此 RocketMQ 没有特意支持消息优先级，但是可以通过变通的方式实现类似功能**，

即单独配置一个优先级高的队列，和一个普通优先级的队列，将不同优先级发送到不同队列即可。

对于优先级问题的支持，可以归纳为 2 类：

1. 只要达到优先级目的即可，不是严格意义上的优先级，通常将优先级划分为高、中、低，或者再多几个级别。
    - 每个优先级可以用不同的 topic 表示，发消息时，指定不同的 topic 来表示优先级，这种方式可以解决绝大部分的优先级问题，但是对业务的优先级精确性做了妥协。
2. 严格的优先级，优先级用整数表示，例如 0 ~ 65535，这种优先级问题一般使用不同 topic 解决就非常不合适。
    - 如果要让 MQ 解决此问题，会对 MQ 的性能造成非常大的影响。
    - 这里要确保一点，业务上是否确实需要这种严格的优先级，如果将优先级压缩成几个，对业务的影响有多大？

### RocketMQ 出现消息重复的原因

QoS：Quality of Service，服务质量。

消息领域有一个对消息投递的 QoS 定义，分为：

- 最多一次（At most once）
- 至少一次（At least once）
- 仅一次（ Exactly once）

几乎所有的 MQ 产品都声称自己做到了 At least once。

既然是至少一次，那避免不了消息重复，尤其是在分布式网络环境下。

比如：网络原因闪断，ACK 返回失败等等故障，确认信息没有传送到消息队列，导致消息队列不知道自己已经消费过该消息了，再次将该消息分发给其他的消费者。

不同的消息队列发送的确认信息形式不同。

- 例如 RabbitMQ 是发送一个 ACK 确认消息，RocketMQ 是返回一个 CONSUME_SUCCESS 成功标志，Kafka 实际上有个 offset 的概念。

RocketMQ 没有内置消息去重的解决方案，最新版本是否支持还需确认。

### RocketMQ 的消息去重

去重原则：

- 使用业务端逻辑保持幂等性

幂等性：

- 就是用户对于同一操作发起的一次请求或者多次请求的结果是一致的；不会因为多次点击而产生了副作用，数据库的结果都是唯一的，不可变的。
- 只要保持幂等性，不管来多少条重复消息，最后处理的结果都一样，需要业务端来实现。

去重策略：

- 保证每条消息都有唯一编号 (比如唯一流水号)，且保证消息处理成功与去重表的日志同时出现。

建立一个消息表，拿到这个消息做数据库的 insert 操作。给这个消息做一个唯一主键（primary key）或者唯一约束，那么就算出现重复消费的情况，就会导致主键冲突，那么就不再处理这条消息。

### RocketMQ 避免重复消费

视频讲解：[两天玩转 RocketMQ-17-重复消费](https://www.bilibili.com/video/BV1uQ4y1d7uS?p=17)

避免客户端扩容

考虑网络抖动的情况

### RocketMQ 幂等性

视频讲解：[两天玩转 RocketMQ-18-幂等性](https://www.bilibili.com/video/BV1uQ4y1d7uS?p=18)

消息幂等：消息重复消费，结果保持一致。

解决重复消费的问题。

使用业务 id 作为消息的 key。

messageId 是 RocketMQ 生成的，并不能保证唯一。

应用层面要考虑使用幂等的 sql，让接口拥有幂等性。

---

## RocketMQ 实现分布式事务

---

### RocketMQ 的 Half Message / 半消息

是指暂不能被 Consumer 消费的消息。

Producer 已经把消息成功发送到了 Broker 端，但此消息被标记为：暂不能投递状态，处于该种状态下的消息称为半消息。

需要 Producer 对消息的二次确认后，Consumer 才能去消费它。

### RocketMQ 的消息回查

由于网络闪段，生产者应用重启等原因。导致 Producer 端一直没有对 Half Message (半消息) 进行二次确认。

这是 Brock 服务器会定时扫描长期处于半消息的消息，会主动询问 Producer 端该消息的最终状态 (Commit 或者 Rollback), 该消息即为消息回查。

消息回查过程：

1. A 服务先发送个 Half Message 给 Brock 端，消息中携带 B 服务即将要 + 100 元的信息。
2. 当 A 服务知道 Half Message 发送成功后，那么开始第 3 步执行本地事务。
3. 执行本地事务 (会有三种情况 1、执行成功。2、执行失败。3、网络等原因导致没有响应)
4. 如果本地事务成功，那么 Product 像 Brock 服务器发送 Commit, 这样 B 服务就可以消费该 message。
5. 如果本地事务失败，那么 Product 像 Brock 服务器发送 Rollback, 那么就会直接删除上面这条半消息。
6. 如果因为网络等原因迟迟没有返回失败还是成功，那么会执行 RocketMQ 的回调接口，来进行事务的回查。

### 分布式事务的主流方案

TCC

2PC

3PC

### RocketMQ 事务消息

视频讲解：[rocketmq 的事务消息原理](https://www.bilibili.com/video/BV1aq4y1K7jB)

RocketMQ 采用的 2PC 的方式提交事务，同时增加一个逻辑补偿来处理而二阶段超时或者失败的消息。

视频讲解：[两天玩转 RocketMQ-03-事务消息](https://www.bilibili.com/video/BV1uQ4y1d7uS?p=3)

通过半消息，避免生产者的消息未到达经纪人服务器的风险，并且经纪人有回查消息的机制，保证数据的一致性。

正常事务消息：

> 生产者发出半消息，得到经纪人的正确响应；
>
> 执行本地事务；
>
> 提交或者回滚事务。

事务消息补偿：

> 如果经纪人迟迟没等到生产者的提交和回滚，就会主动找生产者确认；
>
> 让生产者检查本地事务的状态；
>
> 经纪人根据状态选择回滚和提交。

[基于 RocketMQ 的分布式事务解决方案](https://www.jianshu.com/p/286cac4625b6)

事务消息案例：

扣款前发送预备消息；

收到确认之后执行扣款；

扣款成功之后发送确认消息；

经纪人收到确认消息，让消费者加钱。

### RocketMQ 的事务消息

消息队列 MQ 提供类似 X/Open XA 的分布式事务功能，通过消息队列 MQ 事务消息能达到分布式事务的最终一致。

事务消息的大致流程：正常事务消息的发送和提交、事务消息的补偿流程。

事务消息发送及提交：

- 发送 half 消息
- 服务端响应消息写入结果
- 根据发送结果执行本地事务（如果写入失败，此时 half 消息对业务不可见，本地逻辑不执行）；
- 根据本地事务状态执行 Commit 或 Rollback（Commit 操作生成消息索引，消息对消费者可见）。

事务消息的补偿流程：

- 对没有 Commit/Rollback 的事务消息（pending 状态的消息），从服务端发起一次 “回查”；
- Producer 收到回查消息，检查回查消息对应的本地事务的状态。
- 根据本地事务状态，重新 Commit 或 RollBack

其中，补偿阶段用于解决消息 Commit 或 Rollback 发生超时或者失败的情况。

事务消息状态：

- 提交状态、回滚状态、中间状态；
- TransactionStatus.CommitTransaction：提交事务，它允许消费者消费此消息。
- TransactionStatus.RollbackTransaction：回滚事务，它代表该消息将被删除，不允许被消费。
- TransactionStatus.Unkonwn：中间状态，它代表需要检查消息队列来确定消息状态。

---





---

参考链接：

- [消息中间件 —RocketMQ 消息存储（一）](https://www.jianshu.com/p/b73fdd893f98)
- [《浅入浅出》-RocketMQ ](https://juejin.cn/post/6844904008629354504#heading-29)
- [RocketMQ 使用指南](https://segmentfault.com/a/1190000040591160)
- [一篇文章把 RabbitMQ、RocketMQ、Kafka 三元归一](http://dockone.io/article/2434606)
- []()

---






