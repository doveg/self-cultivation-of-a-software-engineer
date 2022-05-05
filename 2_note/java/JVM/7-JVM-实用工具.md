# JVM

---

## 实用工具

---

### JVM 指令与工具：jstat/jstack/jmap/jconsole/jps/visualVM

视频讲解：[【java】jvm 指令与工具 jstat/jstack/jmap/jconsole/jps/visualVM](https://www.bilibili.com/video/BV1QJ411P78Q)

jmap：

> 查看内存或者 dump 内存详情的工具，排查内存泄露的利器。
>
> dump 内存信息保存为文件：jmap -dump:file=a pid
>
> 打印堆栈信息：jmap -heap pid
>
> OOM 时自动执行 dump：-XX:+HeapDumpOnOutOfMemoryError

jps：

> 进程分析。
>
> 命令：jps

jstat：

> 内存分析。
>
> 例如：jstat -gcutil pid interval，堆内存的使用百分比。
>
> 例如：jstat -gc pid，堆内存占用。

jstack：

> 线程分析。
>
> 例如：jstack pid，查看堆栈信息。

jconsole：

> 可视化界面：内存、线程、类、VM 概要，MBean 等信息。

**visualVM：**

> Java 8 之后的版本不再内置 visualVM。



