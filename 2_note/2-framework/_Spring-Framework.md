# Spring-Framework / Spring Framework

---

## 基础部分

---

### Spring Framework 的原理

IoC

AOP

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

### JDK 动态代理和 CGLIB 的原理

JDK 动态代理：

    利用拦截器 (拦截器必须实现 InvocationHanlder) 加上反射机制生成一个实现代理接口的匿名类，在调用具体方法前调用 InvokeHandler 来处理

CGLiB 动态代理：

    利用 ASM 开源包，对代理对象类的 class 文件加载进来，通过修改其字节码生成子类来处理。

### JDK 动态代理和 CGLIB 的性能

JDK 动态代理：

使用 CGLib 实现动态代理，CGLib 底层采用 ASM 字节码生成框架，使用字节码技术生成代理类，在 jdk6 之前比使用 Java 反射效率要高。

唯一需要注意的是，CGLib 不能对声明为 final 的方法进行代理，因为 CGLib 原理是动态生成被代理类的子类。

CGLIB：

在 jdk6、jdk7、jdk8 逐步对 JDK 动态代理优化之后，在调用次数较少的情况下，JDK 代理效率高于 CGLIB 代理效率，

只有当进行大量调用的时候，jdk6 和 jdk7 比 CGLIB 代理效率低一点，

但是到 jdk8 的时候，jdk 代理效率高于 CGLIB 代理，总之，每一次 jdk 版本升级，jdk 代理效率都得到提升，而 CGLIB 代理消息确有点跟不上步伐。

### Spring 事务传播

事务传播：

    REQUIRED（默认属性） 如果存在一个事务，则支持当前事务。
         如果没有事务则开启一个新的事务。 被设置成这个级别时，会为每一个被调用的方法创建一个逻辑事务域。
         如果前面的方法已经创建了事务，那么后面的方法支持当前的事务，如果当前没有事务会重新建立事务。
    MANDATORY 支持当前事务，如果当前没有事务，就抛出异常。
    NEVER 以非事务方式执行，如果当前存在事务，则抛出异常。
    NOT_SUPPORTED 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
    REQUIRES_NEW 新建事务，如果当前存在事务，把当前事务挂起。
    SUPPORTS 支持当前事务，如果当前没有事务，就以非事务方式执行。
    NESTED 支持当前事务，新增Savepoint点，与当前事务同步提交或回滚。 
         嵌套事务一个非常重要的概念就是内层事务依赖于外层事务。外层事务失败时，会回滚内层事务所做的动作。
         而内层事务操作失败并不会引起外层事务的回滚。 

PROPAGATION_NESTED 与 PROPAGATION_REQUIRES_NEW 的区别：

```
它们非常类似，都像一个嵌套事务，如果不存在一个活动的事务，都会开启一个新的事务。
使用PROPAGATION_REQUIRES_NEW时，内层事务与外层事务就像两个独立的事务一样，一旦内层事务进行了提交后，外层事务不能对其进行回滚。
两个事务互不影响。 
两个事务不是一个真正的嵌套事务。同时它需要JTA 事务管理器的支持。
使用PROPAGATION_NESTED时，外层事务的回滚可以引起内层事务的回滚。而内层事务的异常并不会导致外层事务的回滚，它是一个真正的嵌套事务
```

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
- [JDK 和 CGLIB 动态代理区别](https://www.jianshu.com/p/46d092bb737d)
- []()
- []()
- []()

---



