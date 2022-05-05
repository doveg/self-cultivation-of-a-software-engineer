# JVM

---

## 性能调优

---

### JVM 性能调优

1、监控 GC 的状态

2、生成堆的 dump 文件

3、分析 dump 文件：Eclipse 的静态内存分析工具

4、判断是否需要优化

    如果满足下面的指标，则一般不需要进行 GC：

        Minor GC 执行时间不到 50ms；
        Minor GC 执行不频繁，约 10 秒一次；
        Full GC 执行时间不到 1s；
        Full GC 执行频率不算频繁，不低于 10 分钟 1 次； 

5、调整 GC 类型和内存分配

### JVM 调优参数

在配置较好的机器上（比如多核、大内存），可以为年老代选择并行收集算法： -XX:+UseParallelOldGC 。

### JVM 内存调优

减少 GC 的频率和 Full GC 的次数。

增大年轻代的内存，即调整 Xmn 的大小为 2048m，让 JVM 几乎不发生 Full GC。











---

参考链接：

- [7 种垃圾回收器特点，优劣及使用场景](https://www.jianshu.com/p/5b2721b891c0)
- [【JVM】空间分配担保机制](https://www.cnblogs.com/july-sunny/p/12618054.html)
- [G1 回收器](https://blog.csdn.net/m0_37989980/article/details/112794928)
- [JVM - 各版本默认垃圾收集器](https://juejin.cn/post/7001406102621388831)
- [基于 JDK1.8 的 JVM 内存结构【JVM 篇三】](https://www.cnblogs.com/yichunguo/p/12007038.html)
- [解析 Java 类加载机制](https://www.cnblogs.com/chanshuyi/p/the_java_class_load_mechamism.html)
- [java 的双亲委派模式了解么](https://blog.csdn.net/qq_27828675/article/details/109514389)
- [JVM 性能调优](https://blog.csdn.net/zhan_lang/article/details/88567569)
- [JVM 调优案例详解](https://blog.csdn.net/m0_67393827/article/details/124288488)
- []()
- []()
- []()

---



