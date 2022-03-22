# Spring-Cloud / Spring Cloud 微服务

---

## 基础部分

---

### Spring Cloud 原理

Spring Cloud 是一系列框架的有序集合。

Spring Cloud 能和主流微服务组件集成。

目前流行的方案有：

> Spring Cloud + Zookeeper
>
> Spring Cloud + Netflix（Hystrix、Erika）
>
> Spring Cloud Alibaba

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

---













