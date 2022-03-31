# Spring-Cloud / Spring Cloud 微服务

---

## 基础部分

---

### Spring Cloud 原理

Spring Cloud 是一套微服务规范，是一系列框架的有序集合，能和主流微服务组件集成。

这套规范共有几代实现：

> Spring Cloud + Zookeeper
>
> Spring Cloud + Netflix（Hystrix、Erika）
>
> Spring Cloud Alibaba

Spring Cloud + Netflix：

| # | 名称 | 模块 | 备注 |
|:---:|:---|:---|:---:|
| 00 | Eureka | 服务注册与发现 |  |
| 01 | Fegin | 声明式服务调用 | Feign 内置了 Ribbon |
| 02 | Ribbon | 负载均衡 |  |
| 03 | Hystrix | 服务熔断与降级 |  |
| 04 | Zuul | API 网关 |  |
| 05 | Config | 分布式配置 |  |
| 06 | RabbitMQ | 消息队列 |  |
| 07 | Bus | 事件、消息总线 |  |
|<img width=50px/>|<img width=200px/>|<img width=200px/>|<img width=200px/>|

Spring Cloud Alibaba

| # | 名称 | 模块 | 备注 |
|:---:|:---|:---|:---:|
| 00 | **Nacos Discovery** | 服务注册与发现 | Name and Config Service，读音：/nɑ:kəʊs/ |
| 01 | Dubbo | 声明式服务调用 |  |
| 02 | Spring Cloud Loadbalancer | 负载均衡 |  |
| 03 | Sentinel | 服务熔断与降级 | 哨兵 |
| 04 | Spring Cloud Gateway | API 网关 |  |
| 05 | **Nacos Config** | 分布式配置 | Name and Config Service，读音：/nɑ:kəʊs/ |
| 06 | RocketMQ | 消息队列 |  |
| 07 | Seata | 分布式事务 |  |
| 08 | Alibaba Cloud OSS | 对象存储 |  |
| 09 | Alibaba Cloud Schedulerx | 分布式调度 |  |
|<img width=50px/>|<img width=200px/>|<img width=200px/>|<img width=200px/>|

### 熔断、降级的注意事项

熔断：

1. 自动熔断时机：在什么条件开启熔断，选好降级熔断算法
2. 熔断恢复，恢复的条件
3. 区分核心和非核心功能，注意核心功能和依赖关系，保证系统整体稳定

降级：

1. 自动降级时机：在什么条件自动降级，做好备选方案
2. 降级恢复

降级策略：

1. 超时降级
2. 失败次数降级
3. 故障降级

技术选型：

1. Hystrix
2. Sentinel

### 服务注册与发现 Consul

Zookeeper 和 Consul ：

CP 设计，保证了一致性，集群搭建的时候，某个节点失效，则会进行选举行的 leader，或者半数以上节点不可用，则无法提供服务，因此可用性没法满足

Eureka：

AP 原则，无主从节点，一个节点挂了，自动切换其他节点可以使用，去中心化

### 服务调用 OpenFeign

Feign：

Springcloud 组件中的一个轻量级 Restful 的 HTTP 服务客户端，Feign 内置了 Ribbon，用来做客户端负载均衡，去调用服务注册中心的服务。

Feign 的使用方式是：使用 Feign 的注解定义接口，调用这个接口，就可以调用服务注册中心的服务

OpenFeign：

springcloud 在 Feign 的基础上支持了 SpringMVC 的注解，如 @RequestMapping 等等。

OpenFeign 的 @FeignClient 可以解析 SpringMVC 的 @RequestMapping 注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务。

###

###

###

###

###

###

###

###

---



---

## 提高部分

---

###

###

---






---

参考链接：

- [架构设计之降级和熔断](https://juejin.cn/post/6993125837771898917)
- [注册中心 eureka 与 consul](https://www.jianshu.com/p/8494698d08b2)
- [Feign 和 OpenFeign 两者区别](https://blog.csdn.net/songyinyi/article/details/106191656)
- [Spring Cloud Alibaba Nacos 服务发现](https://www.cnblogs.com/lilb/p/14408065.html)
- []()
- []()

---













