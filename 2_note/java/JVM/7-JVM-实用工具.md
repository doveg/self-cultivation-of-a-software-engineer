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

### jps 查看进程状况工具

可以列出正在运行的虚拟机进程，并显示虚拟机执行主类（Main Class，main()函数所在的类）名称，

以及这些进程的本地虚拟机唯一 ID（LVMID，Local Virtual Machine Identifier）。

它绝对是使用频率最高的JDK 命令行工具，因为其他的 JDK 工具大多需要输入它查询到的 LVMID 来确定要监控的是哪一个虚拟机进程。

jps 可以通过 RMI 协议查询开启了 RMI 服务的远程虚拟机进程状态，参数 hostid 为 RMI 注册表中注册的主机名。

### jstat 监控工具

jstat / JVM Statistics Monitoring Tool：

用于监视虚拟机各种运行状态信息的命令行工具。

它可以显示本地或者远程 虚拟机进程中的类加载、内存、垃圾收集、即时编译等运行时数据，

在没有 GUI 图形界面、只提供了纯文本控制台环境的服务器上，它将是运行期定位虚拟机性能问题的常用工具。

### jinfo

实时查看和调整虚拟机各项参数。

### jmap

jmap / Memory Map for Java：

命令用于生成堆转储快照（一般称为 heapdump 或 dump 文件）。

不使用 jmap 命令，要想获取Java堆转储快照也还有一些比较“暴力”的手段：

**通过 -XX：+HeapDumpOnOutOfMemoryError 参数，可以让虚拟机在内存溢出异常出现之后自动生成堆转储快照文件，**

通过 -XX：+HeapDumpOnCtrlBreak 参数则可以使用 [Ctrl]+[Break] 键让虚拟机生成堆转储快照文件，

又或者在 Linux 系统下通过 Kill -3 命令发送进程退出信号“恐吓”一下虚拟机，也能顺利拿到堆转储快照。

### jhat 分析转存储快照

jhat / JVM Heap Analysis Tool：

内置了一个微型的 HTTP/Web 服务器，生成堆转储快照的分析结果后，可以在浏览器中查看。

一般不会在部署应用程序的服务器上直接分析堆转储快照，即使可以这样做，也会尽量将堆转储快照文件复制到其他机器上进行分析，

因为分析工作是一个耗时而且极为耗费硬件资源的过程，既然都要在其他机器上进行，就没有必要再受命令行工具的限制了。

另外 jhat 的分析功能相对来说比较简陋，后面题目将会介绍到的 VisualVM，

以及专业用于分析堆转储快照文件的 Eclipse Memory Analyzer、IBM HeapAnalyzer 等工具，都能实现比 jhat 更强大专业的分析功能。

### jstack

jstack / Stack Trace for Java：

命令用于生成虚拟机当前时刻的线程快照（一般称为 threaddump 或者 javacore 文件）。

线程快照就是当前虚拟机内每一条线程正在执行的方法堆栈的集合，生成线程快照的目的通常是定位线程出现长

时间停顿的原因，如线程间死锁、死循环、请求外部资源导致的长时间挂起等，都是导致线程长时间停顿的常见原因。

线程出现停顿时通过 jstack 来查看各个线程的调用堆栈，就可以获知没有响应的线程到底在后台做些什么事情，或者等待着什么资源。




---

## 问题排查

---

### 内存泄漏与内存溢出

内存泄漏：

指创建的对象已经没有用处，正常情况下应该会被垃圾收集器回收，但是由于该对象仍然被其他对象进行了无效引用，导致不能够被垃圾收集器及时清理，这种现象称之为内存泄漏。

内存泄漏会导致内存堆积，最终发生内存溢出，导致OOM。

**发生内存泄漏大部分是由于程序代码导致的，排查方法一般是使用 visualVM 进行 heap dump，查看占用空间比较多的 class 对象，**

然后检查该对象的instances 以及 reference引用，最终定位到程序代码。

### 对象逃逸，对象逃逸分析的优化

逃逸分析的基本原理：

分析对象动态作用域，当一个对象在方法里面被定义后，它可能被外部方法所引用，例如作为调用参数传递到其他方法中，这种称为方法逃逸；

甚至还有可能被外部线程访问到，譬如赋值给可以在其他线程中访问的实例变量，这种称为线程逃逸；

从不逃逸、方法逃逸到线程逃逸，称为对象由低到高的不同逃逸程度。

优化有三种：栈上分配；标量替换；锁消除（或称同步消除）。

### 锁粗化

如果虚拟机探测到有这样一串零碎的操作都对同一个对象加锁，将会把加锁同步的范围扩展（粗化）到整个操作序列的外部





