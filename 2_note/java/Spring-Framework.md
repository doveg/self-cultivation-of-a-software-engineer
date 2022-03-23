# Spring-Framework / Spring Framework

---

## 基础部分

---

### Spring Framework 的原理

###

###

###

###
---

## Spring AOP

---

### 对 Spring AOP 的理解

1、连接点：需要增强的方法

2、切点：AOP 通过切点定位连接点

> 切点和连接点： - 连接点相当于数据库表中的记录，而切点相当于查询条件 - 连接点和切点不是一一对应的关系，一个切点可以匹配多个连接点

4、通知：织入到目标类的连接点上的代码

5、引入：添加方法或字段到被通知的类

6、目标对象：要织入的目标类

7、AOP 代理：AOP 框架创建的对象，包含通知

8、织入：将增强添加到目标类具体连接点上的过程

9、切面：由切点和增强组成，它既包括了横切逻辑的定义，也包括了连接点的定义

### Spring AOP 提供了 5 种类型的通知：

1、前置通知（Before）：在目标方法被调用之前调用

2、后置通知（After）：在目标方法完成之后调用，无论该方法是否发生异常

3、后置返回通知（After-returning）：在目标方法成功执行之后调用

4、后置异常通知（After-throwing）：在目标方法抛出异常后调用

5、环绕通知（Around）：通知包裹了被通知的方法，在被通知的方法调用之前和调用之后执行自定义的行为

### Spring 支持生成代理对象的方式

1、JDK 动态代理

2、CGLib / Code Generation Library

默认策略：如果目标类实现了接口，则使用 JDK 动态代理，否则使用 Cglib 生成代理对象




---

## Spring IoC

---

### 什么是 Spring Bean

被 Spring 容器管理的 Java 对象。

### Spring Bean 的生命周期

生成

销毁

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

---

## Spring-BeanFactory

单例

无状态

执行 AbstractApplicationContext 的 refresh 之后，SingletonBeanRegistry 构造出 bean。

refresh 方法是入口方法，会调用诸多子类的方法去做初始化。

BeanDefinition 做 BeanFactory 的后置处理。

AutowireCapableBeanFactory 自动注入能力接口

defaultListableBeanFactory：为 ApplicationContext 提供了 getBean 的能力。 

---

###

---






---

参考链接：

- [一口气说出 6 种，@Transactional 注解的失效场景](https://juejin.cn/post/6844904096747503629)
- [Spring AOP 的理解与使用](https://juejin.cn/post/6901643231537627149)
- [面试还不知道 BeanFactory 和 ApplicationContext 的区别？](https://juejin.cn/post/6844903877574131726)

---









