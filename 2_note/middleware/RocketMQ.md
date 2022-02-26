# RocketMQ

---

## 基础部分

---

### RocketMQ 核心概念

**NameServer / 注册中心**：

- 主要负责对于源数据的管理，包括了对于 Topic 和路由信息的管理。

**Producer / 消息生产者**：

- 负责产生消息，一般由业务系统负责产生消息。

**Broker / 消息中转角色**：

- 负责存储消息，转发消息。

**Consumer / 消息消费者**：

- 负责消费消息，一般是后台系统负责异步消费。

**Message / 消息**：

- 要传输的信息。

**Topic / 话题**：

- 可以看做消息的规类，它是消息的第一级类型。

**Tag / 标签**：

- Tag 可以看作子主题，它是消息的第二级类型，用于为用户提供额外的灵活性。

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

### RocketMQ 的数据存储

RocketMQ 的混合型存储结构针对 Producer 和 Consumer 分别采用了数据和索引部分相分离的存储结构，Producer 发送消息至 Broker 端，然后 Broker
端使用同步或者异步的方式对消息刷盘持久化，保存至 CommitLog 中。

只要消息被刷盘持久化至磁盘文件 CommitLog 中，那么 Producer 发送的消息就不会丢失。

正因为如此，Consumer 也就肯定有机会去消费这条消息，至于消费的时间可以稍微滞后一些也没有太大的关系。

退一步地讲，即使 Consumer 端第一次没法拉取到待消费的消息，Broker 服务端也能够通过长轮询机制等待一定时间延迟后再次发起拉取消息的请求。

具体做法是，使用 Broker 端的后台服务线程 — ReputMessageService 不停地分发请求并异步构建 ConsumeQueue（逻辑消费队列）和 IndexFile（索引文件）数据

然后，Consumer 即可根据 ConsumerQueue 来查找待消费的消息了。

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




---

## 提高部分

---

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

- 就是用户对于同一操作发起的一次请求或者多次请求的结果是一致的，不会因为多次点击而产生了副作用，数据库的结果都是唯一的，不可变的。
  只要保持幂等性，不管来多少条重复消息，最后处理的结果都一样，需要业务端来实现。

去重策略：

- 保证每条消息都有唯一编号 (比如唯一流水号)，且保证消息处理成功与去重表的日志同时出现。

建立一个消息表，拿到这个消息做数据库的 insert 操作。给这个消息做一个唯一主键（primary key）或者唯一约束，那么就算出现重复消费的情况，就会导致主键冲突，那么就不再处理这条消息。

### RocketMQ 避免重复消费

---

## RocketMQ 实现分布式事务

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

###

---





---

参考链接：

- [消息中间件 —RocketMQ 消息存储（一）](https://www.jianshu.com/p/b73fdd893f98)
- [《浅入浅出》-RocketMQ ](https://juejin.cn/post/6844904008629354504#heading-29)
- []()

---






