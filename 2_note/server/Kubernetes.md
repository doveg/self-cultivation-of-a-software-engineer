# Kubernetes

---

## 基础部分

---

### Kubernetes 核心概念

Kubernetes：一个针对容器应用，进行自动部署，弹性伸缩和管理的开源系统。主要功能是生产环境中的容器编排。

### Kubernetes 基本架构

和大多数分布式系统一样，Kubernetes 集群至少需要一个主节点（Master）和多个计算节点（Node）。

主节点主要用于暴露 API，调度部署和节点的管理；

计算节点运行一个容器运行环境，一般是 docker 环境（类似 docker 环境的还有 rkt），同时运行一个 K8s 的代理（kubelet）用于和 master
通信。计算节点也会运行一些额外的组件，像记录日志，节点监控，服务发现等等。计算节点是 k8s 集群中真正工作的节点。

### Kubernetes 具体架构

1、Master 节点（默认不参加实际工作）：

> Kubectl：客户端命令行工具，作为整个 K8s 集群的操作入口；
>
> Api Server：在 K8s 架构中承担的是 “桥梁” 的角色，作为资源操作的唯一入口，它提供了认证、授权、访问控制、API 注册和发现等机制。客户端与 k8s 群集及 K8s 内部组件的通信，都要通过 Api Server 这个组件；
>
> Controller-manager：负责维护群集的状态，比如故障检测、自动扩展、滚动更新等；
>
> Scheduler：负责资源的调度，按照预定的调度策略将 pod 调度到相应的 node 节点上；
>
> Etcd：担任数据中心的角色，保存了整个群集的状态；

2、Node 节点：


> Kubelet：负责维护容器的生命周期，同时也负责 Volume 和网络的管理，一般运行在所有的节点，是 Node 节点的代理，当 Scheduler 确定某个 node 上运行 pod 之后，会将 pod 的具体信息（image，volume）等发送给该节点的 kubelet，kubelet 根据这些信息创建和运行容器，并向 master 返回运行状态。（自动修复功能：如果某个节点中的容器宕机，它会尝试重启该容器，若重启无效，则会将该 pod 杀死，然后重新创建一个容器）；
>
> Kube-proxy：Service 在逻辑上代表了后端的多个 pod。负责为 Service 提供 cluster 内部的服务发现和负载均衡（外界通过 Service 访问 pod 提供的服务时，Service 接收到的请求后就是通过 kube-proxy 来转发到 pod 上的）；
>
> container-runtime：是负责管理运行容器的软件，比如 docker
>
> Pod：是 k8s 集群里面最小的单位。每个 pod 里边可以运行一个或多个 container（容器），如果一个 pod 中有两个 container，那么 container 的 USR（用户）、MNT（挂载点）、PID（进程号）是相互隔离的，UTS（主机名和域名）、IPC（消息队列）、NET（网络栈）是相互共享的。我比较喜欢把 pod 来当做豌豆夹，而豌豆就是 pod 中的 container；

### Kubernetes 和 Docker

Docker 提供容器的生命周期管理和，Docker 镜像构建运行时容器。它的主要优 点是将将软件/应用程序运行所需的设置和依赖项打包到一个容器中，从而实现了可移 植性等优点。

Kubernetes 用于关联和编排在多个主机上运行的容器。

### Kubernetes

### Kubernetes

### Kubernetes

### Kubernetes

### Kubernetes

### Kubernetes

### Kubernetes

### Kubernetes

---

## 提高部分

---

---

---

参考链接：

- []()
- []()
- []()

---

