# Windows10安装MySQL8

---

## 准备工作

安装平台：Windows10。

官网下载 MySQL8 安装包：mysql-8.0.28-winx64。

把解压后的文件夹拖到安装路径。

## 安装过程

### 0、设置数据存储路径

在 mysql-8.0.28-winx64 文件夹下新建文件夹 data 用来存放数据。

### 1、my.ini 配置文件

在 mysql-8.0.28-winx64 文件夹下创建文件：my.ini

把下面的配置拷贝进 my.ini，修改两处路径。

```
[mysqld]
# 设置3306端口
port=3306

# 设置mysql的安装目录
basedir=D:\\mysql-8.0.28-winx64

# 设置mysql数据库的数据的存放目录
datadir=D:\\mysql-8.0.28-winx64\\data

# 允许最大连接数
max_connections=200
# 允许连接失败的次数。这是为了防止有人从该主机试图攻击数据库系统
max_connect_errors=10
# 服务端使用的字符集默认为UTF8
character-set-client-handshake=FALSE
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
init_connect='SET NAMES utf8mb4'
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
default_authentication_plugin=mysql_native_password

[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8mb4

[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8mb4
```

### 2、配置环境变量 path

环境变量加 bin 文件夹路径。例如：D:\mysql-8.0.28-winx64\bin

### 3、初始化

管理员身份运行 cmd，进入安装目录下的 bin 文件夹，执行命令：

```
mysqld --initialize --console
```

命令行运行时会显示初始账户和密码，记下root@locahost：后面的初始密码，后面修改密码时会用到。

### 4、安装

管理员身份运行 cmd，在 bin 目录执行下面的命令：

```
mysqld --install mysql
```

### 5、运行MySQL开启服务命令

```
net start mysql
```

## 其他命令

### 改密码命令

```
mysql -u root -p
```

```
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '新密码';
```

### 卸载命令

先停止 MySQL，命令：

```
net stop mysql
```

再执行卸载命令：

```
sc delete mysql
```

```
mysqld remove mysql
```

### 重装命令

```
mysqld --install mysql
```

---


