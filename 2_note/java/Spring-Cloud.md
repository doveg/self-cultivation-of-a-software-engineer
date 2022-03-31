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

> 服务注册与发现：Eureka
>
> 负载均衡：Ribbon
>
> 声明式服务调用：Fegin。Feign 内置了 Ribbon
>
> 服务熔断与降级：Hystrix
>
> API 网关：Zuul
>
> 分布式配置：Config
>
> 消息队列：RabbitMQ
>
> 事件、消息总线：Bus


Spring Cloud Alibaba

> 服务注册与发现：Nacos Discovery
>
> 负载均衡：Spring Cloud Loadbalancer
>
> 服务调用：Dubbo
>
> 服务熔断与降级：Sentinel
>
> API 网关：Spring Cloud Gateway
>
> 分布式配置：Nacos Config
>
> 分布式事务：Seata

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

### Consul

Zookeeper 和 Consul ：

CP 设计，保证了一致性，集群搭建的时候，某个节点失效，则会进行选举行的 leader，或者半数以上节点不可用，则无法提供服务，因此可用性没法满足

Eureka：

AP 原则，无主从节点，一个节点挂了，自动切换其他节点可以使用，去中心化

### OpenFeign

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
- []()
- []()
- []()

---













