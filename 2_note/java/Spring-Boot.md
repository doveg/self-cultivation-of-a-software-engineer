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

## Spring Schedule

---

### Spring Schedule 简介

Spring Schedule 包含在 spring-boot-starter 基础模块中

@EnableScheduling

Cron 表达式由 6 或 7 个空格分隔的时间字段组成

### 配置 TaskScheduler 线程池

```
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
     
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }
}
```

### Web 应用中的启动和关闭问题

1、当前对象是通过 spring 初始化

spring 在卸载（销毁）实例时，会调用实例的 destroy 方法。通过实现 DisposableBean 接口覆盖 destroy 方法实现。在 destroy 方法中主动关闭线程。

```
@Component
public class MyTask implements DisposableBean{
    @Override
    public void destroy() throws Exception {
        //关闭线程或线程池
        ThreadPoolTaskScheduler scheduler = (ThreadPoolTaskScheduler)applicationContext.getBean("scheduler");
        scheduler.shutdown();
    }
    //省略...
}
```

2、当前对象不是通过 spring 初始化（管理）

那么我们可以增加一个 Servlet 上下文监听器，在 Servlet 服务停止的时候主动关闭线程。

```
public class MyTaskListenter implements ServletContextListener{
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        //关闭线程或线程池
    }
    //省略...
}
```

### 分布式部署问题

在实际项目中，我们的系统通常会做集群、分布式或灾备部署。那么定时任务就可能出现并发问题，即同一个任务在多个服务器上同时在运行。

解决方法（分布式锁）：

1）通过数据库表锁

2）通过缓存中间件

3）通过 Zookeeper 实现

### 动态修改定时任务

实现 SchedulingConfigurer 接口，

覆盖 configureTasks 方法。

```java
public class ScheduledTaskV2 implements SchedulingConfigurer {
    @Value("${demo.corn}")
    private String corn;
    @Value("${demo.cornV2}")
    private String cornV2;
    private int tag = 0;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // 参数传入一个size为10的线程池
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
        // 传入多个任务
        scheduledTaskRegistrar.addTriggerTask(() -> {
            LoggerUtils.info("定时任务V2：" + DateUtils.dateFormat());
        }, (triggerContext) -> {
            CronTrigger cronTrigger;
            if (tag % 2 == 0) {
                LoggerUtils.info("定时任务V2动态修改corn表达式：" + corn + "," + DateUtils.dateFormat());
                cronTrigger = new CronTrigger(corn);
                tag++;
            } else {
                LoggerUtils.info("定时任务V2动态修改corn表达式：" + cornV2 + "," + DateUtils.dateFormat());
                cronTrigger = new CronTrigger(cornV2);
                tag++;
            }
            return cronTrigger.nextExecutionTime(triggerContext);
        });
    }
}
```

### 异步执行

@EnableAsync

---

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









