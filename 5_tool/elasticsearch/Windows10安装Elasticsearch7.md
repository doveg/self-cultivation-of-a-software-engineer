# Windows10安装Elasticsearch7

---

## 准备工作

安装平台：Windows10。

官网下载 Elasticsearch7 安装包：elasticsearch-7.17.1

把解压后的文件夹拖到安装路径。

## 安装过程

### 1、修改配置

在 elasticsearch.yml 文件追加密码验证配置：

```yml
# 开启 x-pack 验证
xpack.security.enabled: true
xpack.license.self_generated.type: basic
xpack.security.transport.ssl.enabled: true
```

### 2、安装

运行 elasticsearch.bat，等待安装完成的提示。

### 3、设置密码

cmd 运行命令：

```
elasticsearch-setup-passwords interactive
```

### 4、elasticsearch 设置为服务

设置为服务之后可以随系统启动，方便管理。

cmd 运行命令：

```
elasticsearch-service.bat install
```

---


