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

### JVM 调优参数

```
-Xms256m：初始化堆大小为 256m；
-Xmx2g：堆最大内存为 2g；
-Xmn50m：新生代的大小50m；
-XX:+PrintGCDetails 打印 gc 详细信息；
-XX:+HeapDumpOnOutOfMemoryError 在发生OutOfMemoryError错误时，来 dump 出堆快照；
-XX:NewRatio=4 设置年轻的和老年代的内存比例为 1:4；
-XX:SurvivorRatio=8 设置新生代 Eden 和 Survivor 比例为 8:2；
-XX:+UseSerialGC 新生代和老年代都用串行收集器 Serial + Serial Old
-XX:+UseParNewGC 指定使用 ParNew + Serial Old 垃圾回收器组合；
-XX:+UseParallelGC 新生代使用 Parallel Scavenge，老年代使用 Serial Old
-XX:+UseParallelOldGC：新生代 ParallelScavenge + 老年代 ParallelOld 组合；
-XX:+UseConcMarkSweepGC：新生代使用 ParNew，老年代的用 CMS；
-XX:NewSize：新生代最小值；
-XX:MaxNewSize：新生代最大值
-XX:MetaspaceSize 元空间初始化大小
-XX:MaxMetaspaceSize 元空间最大值
```

### JVM 调优经验

可以从如下几个大方向进行设计：

在大访问压力下，MinorGC 频繁，MinorGC 是针对新生代进行回收的，每次在MGC 存活下来的对象，会移动到Survivor1区。先到这里为止，大访问压力下，MGC 频繁一些是正常的，只要MGC 延迟不导致停顿时间过长或者引发FGC ，那可以适当的增大Eden
空间大小，降低频繁程度，同时要保证，空间增大对垃圾回收时间产生的停顿时间增长也是可以接受的。

由于大对象创建频繁，导致Full GC 频繁。对于大对象，JVM专门有参数进行控制，-XX：PretenureSizeThreshold。超过这个参数值的对象，会直接进入老年代，只能等到full GC
进行回收，所以在系统压测过程中，要重点监测大对象的产生。如果能够优化对象大小，则进行代码层面的优化，优化如：根据业务需求看是否可以将该大对象设置为单例模式下的对象，或者该大对象是否可以进行拆分使用，或者如果大对象确定使用完成后，将该对象赋值为null，方便垃圾回收。如果代码层面无法优化，则需要考虑：a:
调高-XX：PretenureSizeThreshold参数的大小，使对象有机会在eden区创建，有机会经历MGC以被回收。但是这个参数的调整要结合MGC过程中Eden区的大小是否能够承载，包括S1区的大小承载问题。b：这是最不希望发生的情况，如果必须要进入老年代，也要尽量保证，该对象确实是长时间使用的对象，放入老年代的总对象创建量不会造成老年代的内存空间迅速长满发生Full
GC，在这种情况下，可以通过定时脚本，在业务系统不繁忙情况下，主动触发full gc。

MGC 与 FGC 停顿时间长导致影响用户体验。其实对于停顿时间长的问题无非就两种情况：a：gc 真实回收过程时间长，即real time时间长。这种时间长大部分是因为内存过大导致，导致从标记到清理的过程中需要对很大的空间进行操作，导致停顿时间长。b：gc真实回收时间 real
time 并不长，但是user time(
用户态执行时间) 和sys time（核心态执行时间）时间长，导致从客户角度来看，停顿时间过长。对于a情况，要考虑减少堆内存大小，包括新生代和老年代，比如之前使用16G的堆内存，可以考虑将16G
内存拆分为4个4G的内存区域，可以单台机器部署JVM逻辑集群，也可以为了降低GC回收时间进行4节点的分布式部署，这里的分布式部署是为了降低GC垃圾回收时间。对于b情况，要考虑线程是否及时达到了安全点，通过-XX：+PrintSafepointStatistics和-XX：PrintSafepointStatisticsCount=1去查看安全点日志，如果有长时间未达到安全点的线程，再通过参数-XX：+SafepointTimeout和-XX：SafepointTimeoutDelay=2000两个参数来找到大于2000ms到达安全点的线程，这里的2000ms可以根据情况自己设置，然后对代码进行针对的调整。除了安全点问题，也有可能是操作系统本身负载比较高，导致处理速度过慢，线程达到安全点时间长，因此需要同时检测操作系统自身的运行情况。

内存泄漏导致的MGC和FGC频繁，最终引发oom。

纯代码级别导致的MGC和FGC频繁。如果是这种情况，那就只能对代码进行大范围的调整，这种情况就非常多了，而且会很糟糕。如大循环体中的new 对象，未使用合理容器进行对象托管导致对象创建频繁，不合理的数据结构使用等等。

总之，JVM的调优无非就一个目的，在系统可接受的情况下达到一个合理的MGC和FGC的频率以及可接受的回收时间。














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



