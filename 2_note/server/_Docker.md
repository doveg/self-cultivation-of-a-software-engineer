# Docker

---

## 基础部分

---

### Docker 核心概念

视频讲解：[Docker 10 分钟快速入门](https://www.bilibili.com/video/BV1s54y1n7Ev)

视频讲解：[【docker 入门】10 分钟，快速学会 docker](https://www.bilibili.com/video/BV1R4411F7t9)

Docker：

一个容器化平台，它以容器的形式将应用程序及其所有依赖项打包在一起。

**Dockerfile：**

- 可以在命令行上调用，用来创建镜像的命令文档。

**image / 镜像：**

- Docker 容器的源代码，Docker 镜像用于创建容器。

**container / 容器：**

- 包括应用程序及其所有依赖项，作为操作系统的独立进程运行。

**Volume / 数据卷：**

挂载到容器中的路径。

**Docker Compose：**

Docker 编排工具，用来管理多个容器。

Docker Swarm：

> 容器调度平台

> Docker 的本机群集。它将 Docker 主机池转变为单个虚拟 Docker 主机。

> Docker Swarm 提供标准的 Docker API，任何已经与 Docker 守护进程通信的工具都可以使用 Swarm 透明地扩展到多个主机。

仓库：

> 可以从仓库下载已经制作好的镜像。

---

## 提高部分

---

###

###

###

---

## 入门部分

---

### Dockerfile 指令

| # | 关键字 | 简介 | 推荐指数 |
|:---:|:---|:---|:---:|
| 00 | ARG | 构建参数 |  |
| 01 | ADD | 更高级的复制文件 |  |
| 02 | COPY | 复制文件 |  |
| 03 | **CMD** | 容器启动命令 |  |
| 04 | ENTRYPOINT | 入口点 |  |
| 05 | **ENV** | 设置环境变量 |  |
| 06 | **EXPOSE** | 暴露端口 |  |
| 07 | **FROM** | 指定基础镜像 |  |
| 08 | **HEALTHCHECK** | 健康检查 |  |
| 09 | LABEL | 为镜像添加元数据 |  |
| 10 | ONBUILD | 为他人作嫁衣裳 |  |
| 11 | SHELL | 指令 |  |
| 12 | USER | 指定当前用户 |  |
| 13 | **VOLUME** | 定义匿名卷 |  |
| 14 | **WORKDIR** | 指定工作目录 |  |
|<img width=50px/>|<img width=200px/>|<img width=400px/>|<img width=100px/>|

### Docker 常用命令

| # | 命令 | 简介 | 推荐指数 |
|:---:|:---|:---|:---:|
| 00 | docker container run | 新建容器 |  |
| 01 | docker container stop | 终止容器运行 |  |
| 02 | docker container logs | 查看 docker 容器的输出 |  |
| 03 | docker container exec | 进入一个正在运行的 docker 容器 |  |
| 04 | docker container cp | 从正在运行的 Docker 容器里面，将文件拷贝到本机 |  |
|<img width=50px/>|<img width=200px/>|<img width=400px/>|<img width=100px/>|

###

###

###

###

---

参考链接：

- []()
- []()
- []()
- []()
- []()

---

