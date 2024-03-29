# Spark

---

## 基础部分

### 为什么不用 Hive

Hive 的出现弥补了 Hadoop 只能用来做离线数据处理这个缺陷，提供了一种常用的分析接口，并且提供了非常好的用户交互方式。

Hive 提供 JDBC 接口实现支持以编程形式进行交互，同时业内几乎所有 SQL Client、开源或商业 BI 工具都支持通过标准 JDBC 的方式连接
Hive，可以支持数据探索的动作，极大地丰富了大数据生态圈下的组件多样性，同时也降低了使用门槛，可以让熟悉 SQL 的人员低成本迁移。

基于这些设计非常好的特效，加上 Hive 经过多年的逐步完善，发展到今天已经是一个非常稳定成熟的生产环境可用的数据仓库组件，甚至替代品都很难找到，因此使用 Hive
作为数据仓库的构建基础是一个非常好的选择。

Hive 的优点：

> 稳定：稳定性是 Hive 一个非常让人称道的特性，很多时候虽然 Hive 的性能，计算速度不及其他引擎，但是 Hive 的稳定性却一直是非常好的。
>
> 低门槛：只需要掌握基本的 SQL 技能，便可使用 Hive 进行开发，相比其他分布式计算引擎引擎成本更低。
>
> 生态丰富：Hive 和 Hadoop 生态圈紧密结合，而 Hive 自身的 Metastore 也成了大数据生态圈内的标准元数据服务，大部分引擎都支持直接适配 MetaStore。
>
> 扩展方便：Hive 自身的 UDF 机制可以快速基于业务需要扩展功能。
>
> 安全：Hive 支持 Kerberos/LDAP 多种认证方式，并且和 Ranger 结合可以做到更细粒度的行列权限级别，拥有较好的数据安全。
>
> 集成成本低：MapReduce 只支持编程态的接口，并且不支持迭代计算，Hive 封装了 MapReduce 提供 SQL 的接口，可以很低成本的和上层数据挖掘，数据分析工具进行集成。

所以，虽然 Hive 出现已经有很长时间了，但依旧是数仓构建的首选，在整个数仓构建中随处可见 Hive 的身影。尽管 Hive
有种种优点，让人难以割舍，但是并不等于能很好地支撑企业业务需求。很多时候选择 Hive
仅仅是因为暂时没有其他可选的组件，如果自己从头开发一个，或者基于某个组件改造，成本又会远超企业预期，因此不得不继续选择使用 Hive。

基于实践来看，Hive 在构建企业数仓过程中，**局限**主要在以下几个方面：

> 性能：Hive 基于 MapReduce 虽然带来了非常好的稳定性，同时也降低了它的性能，虽然有 TEZ 做一定的优化，但是与同类的计算引擎 Spark 相比依旧有非常大的差距。
>
> 资源配置：由于 Hive 底层使用 MapReduce 作为计算引擎，而 MapReduce 对 SQL 不友好，因此 Hive 在 HiveServer2 层面实现了 SQL 的转换处理，再生成基于 MapReduce 的物理计划，从而导致 HiveServer2 需要非常高的配置，才能维持足够好的稳定性。
>
> 并发：Hive 的并发受限于 HiveServer2，企业需要维护多个高配的 HiveServer2 实例才能支持更好的并非，通常 Hive 的瓶颈都在 HiveServer2 而不是更底层的分布式计算。
>
> 容错成本：Hive 基于 HiveServer2 进行 SQL 的分析处理，多个 HiveServer2 之间相互独立不共享信息，因此当 HiveServer2 挂掉后，整个 HiveServer2 的任务都会结束，需要客户端自行重试，为整个作业级别的容错重启。
>
> 事务支持：Hive 的事务设置在 HiveServer2 上，一旦 HiveServer2 实例开启事务后，整个通过该 HiveServer2 的请求都会开启事务，整个事务成本过高。
>
> 部署：如果企业的计算引擎部署是基于 K8S 等容器架构，Hive on K8S 将会带来非常大的部署成本。

虽然 Hive 在以上局限层面也做了很多尝试（Hive On Spark），但是受限于 Hive 的架构，HiveServer2 自身有自己的 SQL 解析引擎，为了兼容架构将解析后的结果直接翻译成
Spark 最底层的接口，整体性能反而提升不大。

除了 Hive 之外，还有非常多其他的优秀组件。然而，从企业数仓技术选型的视角来看，适合用来构建数据仓库的，目前只有 Hive 和 Spark SQL 相对更加合适，在这两个组件中，Spark SQL
相对 Hive 的优势又更加明显。

### 为什么用 SparkSQL

Spark 引擎因为自身强大的生态和方便的编程接口被广泛应用在数据处理场景下，Spark 提供的 Spark SQL 模块更是为使用 Spark 支撑企业数据仓库提供了一个良好的基础设施。

一个典型的数据仓库架构需要包含不同层次的模型构建。由于数据量大、数据结构异构等多种原因，大数据架构下的企业数仓构建抛弃了基于关系型数据库下的 Cube
设计，直接采用基于分布式任务进行处理来构建多层数据模型。

因此对于构建企业数仓的服务来说，有着如下要求：

> 支持长时任务，通常是小时以上，天级别居多。
>
> 支持多任务，也就是高并发。
>
> 稳定性必须被保障。
>
> 速度快。
>
> 支持 SQL 的交互式接口。
>
> 易于集成。
>
> 支持任务的重跑和容错以及快速任务失败恢复。

基于以上特性可以发现，在目前可选择的组件范围内，Spark SQL 相比其他组件（乃至 Hive）更加适合承担这类任务。但是很多企业在进行架构设计的时候割舍不掉 Spark SQL
带来的丰富特性，又愁于 Spark SQL 缺乏类似 Hive 这样的 SQL 服务器，于是退而求其次变成 Hive 与 Spark SQL 两个组件共存的形态，Hive 退化为仅仅提供
MetaStore 服务，因此从很多实践的现象来看，Hive 构建企业数仓已是过去式，采用 Spark SQL 进行数据仓库的构建是众多的选择。

企业在构建数仓的时候，通过一个 Spark SQL Server 提供基于 SQL 接口的常驻服务，同时也可以采用 Spark Submit 的方式直接提交 Jar 任务去运行，既能达到提供标准
SQL 交互式接口，又能提供更灵活的编程态接口。

从不同的企业级数仓构建视角来看，Hive 带来的约束都越来越大，而 Spark SQL 的成熟度和发展趋势已经完全具备取代 Hive 来构建整个数仓。

Spark SQL 的优势集中体现在如下方面：

> 丰富的生态：Spark 不仅可以和很多组件集成，其自身拥有生态已经涵盖各个方面，从数据分析到机器学习和图计算。
>
> 开放：Spark 架构设计上非常开放，可以快速整合其他产品，例如相比 Hive，在集成 Iceberg，Hudi 等特性方面就会开放很多。
>
> 部署：Spark 既可以部署在 ECS 虚拟机上，也可部署在 K8S 架构上，多种部署形态非常灵活。
>
> 性能：Spark 的机制的流批处理性能非常合适用来构建企业数仓。
>
> 易于开发：Spark SQL 既有 SQL 接口，也支持灵活的可迭代编程接口，非常方便不同场景下的数据开发。
>
> 安全：Spark SQL 可和不同的安全服务集成，实现细粒度的鉴权。

因此，完全基于使用 Spark SQL 来支撑企业级的数仓是完全可行的，并且在目前也被众多企业实践验证。

一个基于 Spark SQL 构建的企业数仓架构逻辑架构设计上包含以上几个部分，每一个 Spark SQL 引擎都是一个服务器，Spark SQL 引擎将自己的信息注册到 Zookeeper
中，SQL 服务器基于 Zookeeper 中的 Spark SQL 引擎来执行客户端过来的请求，SQL 服务器是一个兼容 Hive JDBC 接口的服务器。

使用 Spark SQL 来支撑数仓构建的时，需要重点考虑的实施点：

> 如何提供一个交互服务用来支撑不同的客户端来连接，包括交互式的 beeline，以及编程态的 JDBC 和工具接口。
>
> 如何打通权限对接，如果是 Ranger 的话需要的是 Spark SQL Ranger Plugin。
>
> 如何支持跨多个队列的任务提交。

使用 Spark SQL 支撑企业级数仓的核心之处还是在于如何提供一个好用的任务服务器，用来支撑任务的管理。任务管理服务器在逻辑上与 HiveServer2 相似，但是更加的轻量，没有
HiveServe2 中复杂而繁重的 SQL 解析，同时又没有 Spark Thrift Server 这种自身就是一个 YARN
作业的约束。企业可以基于自身的业务流程，开发一个轻量的服务器，在这方面字节有非常深的实践经验，同时也有自己的 Spark SQL
引擎服务器，可关注后续的动态。同时业界也有其他企业做了类似的工作，例如网易开源的 Kyuubi。

Kyuubi 整个架构图如上所示，Kyuubi 基于 Spark SQL 之上，较好地弥补了 Spark Thrift Server
在多租户、资源隔离和高可用等方面的不足，是一个真正可以满足大多数生产环境场景的开源项目。但是 Kyuubi 在设计时考虑的是如何弥补 Spark Thrift Server 的不足，目的在于增强
Spark SQL 的能力，而不是对等设计一个可以替换 Hive 组件的服务。因此对于遗留项目来说迁移成本较高，Spark SQL 与 Hive 有着两套不兼容的 SQL，在使用 Kyuubi
的时候如何降低遗留系统的迁移成本将是一个非常大的工作量。

而行业也有开源的 Spark Thrift Server，该思路是非常优秀的，但是因为开发过程中有点太过于局限，导致依旧存在很多问题，主要体现在：

> Driver 单点：整个 Spark thrift server 以一个 Spark 任务的形式运行在 YARN 上，所有的请求都运行在一个 Driver 中，一旦 Driver 挂掉后，所有任务都会同时失败。
>
> 资源隔离：因为 Spark thrift server 是以 Spark 任务的形式运行在 YARN 上，因此提交的任务如果有跨队列提交需求的时候，Spark thrift server 很难支撑，其次多个任务运行在同一个 Driver 之中，资源使用会相互影响，很难更精细化的进行资源的管理。
>
> 多租户：Spark thrift server 从请求层面是可以支持多用户的，但是从架构层面来看 Spark thrift server 是一个运行在 Yarn 上的任务，它也有自己的 Application Id 有自己的任务提交者，因此它实际上是以一个超级管理员的身份运行，再做二次租户隔离，必然存在一定的资源安全问题。
>
> 高可用：Spark thrift server 本身是没有高可用涉及的，因此它的高可用需要自行单独设计，且还得考虑客户端的兼容，例如 Hive JDBC 将 HA 信息存储在 ZK 中，而 Spark thrift server 没有这样的机制，因此高可用的实施成本较高。

因此虽然 Spark 提供了 Spark thrift server 服务用来提供类似 JDBC 这样的接口交互方式，但是目前依旧缺乏很多生成功能，导致在生产环境使用的情况非常少，Spark
thrift server 更像是一个小众的半成品，小修小补地尝试着解决部分问题，但是没有给予一个彻底的方案，导致现在有点缺乏实际的生产应用。


---

### 索引

---

## 提高部分

---

###

---



---

参考链接：

- [SparkSQL 在企业级数仓建设的优势 ](https://mp.weixin.qq.com/s/hKAbcwX_pqdzD_4ANT7htw)
- []()

---





