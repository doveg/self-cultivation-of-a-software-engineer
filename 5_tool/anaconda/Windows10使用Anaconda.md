# Windows10使用Anaconda

---

## Anaconda 安装

---

## 安装

官网下载安装包：https://www.anaconda.com

自定义安装路径，注册为默认的 Python 环境。

## 验证

打开 Anaconda Powershell Prompt，输入命令：

```
conda -V
```

---

## Anaconda 命令实例

---

### 查看 Anaconda 虚拟环境列表

```
conda env list
```

### 激活某个虚拟环境

```
conda activate xxx-env
```

### 在特定虚拟环境安装依赖包

由于 Anaconda 官方源的包库不全，有时需要使用 pip 安装。

先激活要安装的环境，然后直接 pip，把包安装到该环境里。

```
pip install xxx
```

---


