# Spring-Boot-core / Spring Boot 核心

---

## 基础部分

---

### Spring Boot 的原理

Spring Boot 能够帮助使用 Spring Framework 生态的开发者快速高效的构建一个基于 Spring 以及 spring 生态体系的应用。

Spring Boot 是一个约定大于配置，开箱即用的集成框架。

是能快速搭建工程的脚手架，能够非常方便的集成其他框架和组件，是 Spring 家族的基石。

### SpringBoot 自动配置的原理

1、SpringBoot 启动会加载大量的自动配置类：

> SpringBootApplication 注解
>
> EnableAutoConfiguration 注解
>
> AutoConfigurationImportSelector 类：往 Spring 容器中导入组件

2、xxxxAutoConfigurartion 自动配置类：给容器中添加组件

3、xxxxProperties：封装配置文件中相关属性

---

## 注解

---

### @Transaction 注解的失效场景

1、用在非 public 修饰的方法上

> 因为 Spring AOP 会检查目标方法的修饰符是否为 public，不是 public 则不会获取 @Transactional 的属性配置信息
>
> 这种情况事务会失效，并且不会报错

2、被同一个类中方法调用 - Spring AOP 有一个比较大的坑：只有被注解的方法，被**非当前类**调用时，才会由 Spring 生成的代理对象来管理

3、数据库引擎不支持事务

---

## 提高部分

---

###

---






---

参考链接：

- [SpringBoot | 自动配置原理](https://juejin.cn/post/6844903812788912141)
- [一口气说出 6 种，@Transactional 注解的失效场景](https://juejin.cn/post/6844904096747503629)
- []()

---









