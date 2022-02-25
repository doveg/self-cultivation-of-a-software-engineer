# Spring-Boot-annotation / Spring Boot 注解

---

## 基础部分

---

### @Transaction 注解的失效场景

1. 用在非 public 修饰的方法上
    - 因为 Spring AOP 会检查目标方法的修饰符是否为 public，不是 public 则不会获取 @Transactional 的属性配置信息
    - 这种情况事务会失效，并且不会报错


2. 被同一个类中方法调用
    - Spring AOP 有一个比较大的坑：只有被注解的方法，被**非当前类**调用时，才会由 Spring 生成的代理对象来管理


3. 数据库引擎不支持事务

---

## 提高部分

---

###

---






---

参考链接：

- [一口气说出 6 种，@Transactional 注解的失效场景](https://juejin.cn/post/6844904096747503629)

---









