# Spring-Boot-IOC / Spring Boot 依赖注入

---

## 基础部分

---

### 什么是 Spring Bean

被 Spring 容器管理的 Java 对象

### IoC 容器的初始化

1. BeanDefinition 的 Resource 定位


2. BeanDefinition 的载入
    - 把用户定义好的 Bean 表示成 IoC 容器内部的数据结构，即 BeanDefinition
    - BeanDefinition 是 POJO 对象在 IoC 容器中的抽象，通过 BeanDefinition 定义的数据结构，使 IoC 容器能够方便的管理 Bean


3. BeanDefinition 的注册
    - 在 IoC 容器内部将 BeanDefinition 注入到一个 HashMap 中

### BeanFactory 和 ApplicationContext 的区别

1. 核心区别：

    - ApplicationContext 包含 BeanFactory 的所有特性，通常推荐使用前者
    - 但是 BeanFactory 更加轻量级，对内存要求高可以考虑 BeanFactory


2. BeanFactory：
    - BeanFactory 是访问 Bean 容器的底层接口
    - 包含了 Bean 的定义，读取 Bean 配置文档，管理 Bean 的加载、实例化，控制 Bean 的生命周期，维护 Bean 之间的依赖关系
    - BeanFactory 的实现方式是是懒加载


3. ApplicationContext：
    - ApplicationContext 是 Spring 的中央接口
    - **ApplicationContext 继承了 BeanFactory 接口**，所以 ApplicationContext 包含 BeanFactory 的所有功能
    - ApplicationContext 在容器启动时，一次性创建所有的 Bean

### SpringBoot 自动配置的原理

1. SpringBoot 启动会加载大量的自动配置类：
    - SpringBootApplication 注解
    - EnableAutoConfiguration 注解
    - AutoConfigurationImportSelector 类：往 Spring 容器中导入组件


2. xxxxAutoConfigurartion 自动配置类：给容器中添加组件


3. xxxxProperties：封装配置文件中相关属性

---

## 提高部分

---

###

---






---

参考链接：

- [面试还不知道 BeanFactory 和 ApplicationContext 的区别？](https://juejin.cn/post/6844903877574131726)
- [SpringBoot | 自动配置原理](https://juejin.cn/post/6844903812788912141)

---


















