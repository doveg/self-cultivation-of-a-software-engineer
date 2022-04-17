# Spring-Boot-core / Spring Boot 核心

---

## 基础部分

---

### Spring Boot 的原理

Spring Boot 能够帮助使用 Spring Framework 生态的开发者快速高效的构建一个基于 Spring 以及 spring 生态体系的应用。

Spring Boot 是一个约定大于配置，开箱即用的集成框架。

是能快速搭建工程的脚手架，能够非常方便的集成其他框架和组件，是 Spring 家族的基石。

### Spring Boot 自动配置的原理

1、Spring Boot 启动会加载大量的自动配置类：

> SpringBootApplication 注解
>
> EnableAutoConfiguration 注解
>
> AutoConfigurationImportSelector 类：往 Spring 容器中导入组件

2、xxxxAutoConfigurartion 自动配置类：给容器中添加组件

3、xxxxProperties：封装配置文件中相关属性

### spring-boot-starter 作用

它提供了一个自动化配置类，一般命名为 XXXAutoConfiguration，在这个配置类中通过条件注解来决定一个配置是否生效（条件注解就是 Spring 中原本就有的）。

它还会提供一系列的默认配置，也允许开发者根据实际情况自定义相关配置。

通过类型安全的属性注入将这些配置属性注入进来，新注入的属性会代替掉默认属性。

正因为如此，很多第三方框架，我们只需要引入依赖就可以直接使用了。

当然，开发者也可以自定义 Starter。

### spring-boot-starter-parent 作用

新创建一个 SpringBoot 项目，默认都是有 parent 的。

spring-boot-starter-parent 主要有如下作用：

    定义了 Java 编译版本。
    使用 UTF-8 格式编码。
    继承自 spring-boot-dependencies，这个里边定义了依赖的版本，也正是因为继承了这个依赖，所以我们在写依赖时才不需要写版本号。
    执行打包操作的配置。
    自动化的资源过滤。
    自动化的插件配置。
    针对 application.properties 和 application.yml 的资源过滤，包括通过 profile 定义的不同环境的配置文件，例如 application-dev.properties 和 application-dev.yml。

### Spring Boot 的核心注解

启动类上面的注解是 @SpringBootApplication，是 Spring Boot 的核心注解，主要组合包含了以下 3 个注解：

    @SpringBootConfifiguration：组合了 @Confifiguration 注解，实现配置文件的功能。 
    @EnableAutoConfifiguration：打开自动配置的功能，也可以关闭某个自动配置的选项。
        例如： java 如关闭数据源自动配置功能： @SpringBootApplication (exclude = { DataSourceAutoConfiguration.class})。 
    @ComponentScan：Spring 组件扫描。

###

###

---

## 注解

---

### @Transaction 注解的失效场景

**1、用在非 public 的方法上。**

> 因为 Spring AOP 会检查目标方法的修饰符是否为 public，不是 public 则不会获取 @Transactional 的属性配置信息
>
> 这种情况事务会失效，并且不会报错

**2、被同一个类中方法调用。**

> Spring AOP 有一个比较大的坑：只有被注解的方法，被**非当前类**调用时，才会由 Spring 生成的代理对象来管理。

**3、数据库引擎不支持事务。**

> 比如 MySQL 的 MyISAM 引擎就不支持事务，也不支持行级锁和外键。

---

## Spring Schedule

---

### Spring Schedule 简介

Spring Schedule 包含在 spring-boot-starter 基础模块中。

@EnableScheduling

Cron 表达式由 6 或 7 个空格分隔的时间字段组成。

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

### 启动和关闭

1、当前对象是通过 Spring 初始化。

Spring 在卸载（销毁）实例时，会调用实例的 destroy 方法。通过实现 DisposableBean 接口覆盖 destroy 方法实现。在 destroy 方法中主动关闭线程。

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

2、当前对象不是通过 Spring 初始化（管理）。

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

### 实现分布式部署

在实际项目中，我们的系统通常会做集群、分布式或灾备部署。那么定时任务就可能出现并发问题，即同一个任务在多个服务器上同时在运行。

解决方法（分布式锁）：

1、通过数据库表锁。

2、通过缓存中间件。

3、通过 Zookeeper 实现。

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

- [Spring Boot 自动配置原理](https://juejin.cn/post/6844903812788912141)
- [一口气说出 6 种，@Transactional 注解的失效场景](https://juejin.cn/post/6844904096747503629)
- []()
- []()
- []()
- []()

---









