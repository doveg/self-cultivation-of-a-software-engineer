## RocketMQ

---

#### 基础部分

###### RocketMQ 的数据存储

- RocketMQ 的混合型存储结构针对 Producer 和 Consumer 分别采用了数据和索引部分相分离的存储结构，Producer 发送消息至 Broker 端，然后 Broker
  端使用同步或者异步的方式对消息刷盘持久化，保存至 CommitLog 中。
- 只要消息被刷盘持久化至磁盘文件 CommitLog 中，那么 Producer 发送的消息就不会丢失。
- 正因为如此，Consumer 也就肯定有机会去消费这条消息，至于消费的时间可以稍微滞后一些也没有太大的关系。
- 退一步地讲，即使 Consumer 端第一次没法拉取到待消费的消息，Broker 服务端也能够通过长轮询机制等待一定时间延迟后再次发起拉取消息的请求。
- 具体做法是，使用 Broker 端的后台服务线程 —ReputMessageService 不停地分发请求并异步构建 ConsumeQueue（逻辑消费队列）和 IndexFile（索引文件）数据
- 然后，Consumer 即可根据 ConsumerQueue 来查找待消费的消息了。
- 其中，ConsumeQueue（逻辑消费队列）作为消费消息的索引，保存了指定 Topic 下的队列消息在 CommitLog 中的起始物理偏移量 offset，消息大小 size 和消息 Tag
  的 HashCode 值。
- 而 IndexFile（索引文件）则只是为了消息查询提供了一种通过 key 或时间区间来查询消息的方法（ps：这种通过 IndexFile 来查找消息的方法不影响发送与消费消息的主流程）

###### RocketMQ 和 Kafka 的数据存储

- RocketMQ 采用的是混合型的存储结构，即为 Broker 单个实例下所有的队列共用一个日志数据文件（即为 CommitLog）来存储。
- RocketMQ 采用混合型存储结构的缺点在于，会存在较多的随机读操作，因此读的效率偏低。
- 同时消费消息需要依赖 ConsumeQueue，构建该逻辑消费队列需要一定开销。
- Kafka 采用的是独立型的存储结构，每个队列一个文件。

###### RocketMQ 消息消费的流程

###### RocketMQ 避免重复消费

###### RocketMQ 消息丢失的解决办法

###### RocketMQ 消息消费的可靠性

---

#### 提高部分

######

---





---

参考链接：

- [消息中间件 —RocketMQ 消息存储（一）](https://www.jianshu.com/p/b73fdd893f98)
- []()

---






