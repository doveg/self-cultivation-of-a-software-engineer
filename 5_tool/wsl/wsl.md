# WSL / Windows Subsystem for Linux

---

## WSL2 入门

---

### WSL2 安装

1、启用 WSL：

> PS C:\Windows\system32> dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart

2、启用 HyperV：

> PS C:\Windows\system32> dism.exe /online /enable-feature /featurename:VirutalMachinePlatform /all /norestart

3、重启 Windows 操作系统。

4、官网下载安装包 wsl_update_x64.msi，升级到 WSL2。

5、设置默认安装版本：

> wsl --set-default-version 2

6、在应用商店安装发行版。

7、验证安装：

> wsl -l

### WSL2 Ubuntu 迁移安装目录

```
wsl --shutdown

wsl --export Ubuntu-18.04 D:/Ubuntu.tar

wsl --unregister Ubuntu-18.04

wsl --import Ubuntu-18.04 D:\2-software\09-server-build\wsl-ubuntu\ D:\Ubuntu.tar --version 2
```

### WSL2 Debian 迁移安装目录

```
wsl --shutdown

wsl --export Debian D:\Debian.tar

wsl --unregister Debian

wsl --import Debian D:\2-software\09-server-build\wsl-debian D:\Debian.tar --version 2
```

### 设置默认发行版

已安装列表：

```
wslconfig /l
```

设置默认发行版：

```
wslconfig /setdefault Ubuntu-18.04
```

###

---

## Ubuntu 使用

---

### 系统整备

更新软件列表：

```
sudo apt-get upgrade
```

更新软件

```
sudo apt-get upgrade
```

### 安装 Docker

```
curl -skSL https://mirror.azure.cn/repo/install-docker-ce.sh | sh -s -- --mirror AzureChinaCloud
```

### 启动Docker，查看 Docker 版本

```
sudo service docker start
sudo docker version
```

### Docker 验证

```
sudo docker run hello-world
```

---

## WSL2 使用

---



---

---


