# Nginx

---

## 基础部分

---

### 为什么要用 Nginx

高并发连接：处理 2-3 万并发连接数，官方监测能支持 5 万并发。

因为他的事件处理机制：异步非阻塞事件处理机制：运用了 epoll 模型，提供了一个队列，排队解决。

### 正向代理和反向代理

正向代理：一个人发送一个请求直接就到达了目标的服务器。

反方代理：请求统一被 Nginx 接收，nginx 反向代理服务器接收到之后，按照一定的规 则分发给了后端的业务处理服务器进行处理了。

### 应用场景

反向代理，负载均衡。

    当网站的访问量达到一定程度后，单台服务器不能满足用户的请求时，需要用多台服务器集群可以使用 nginx做反向代理。
    并且多台服务器可以平均分担负载，不会应为某台服务器负载高宕机而某台服务器闲置的情况。

http 服务器。Nginx 是一个 http 服务可以独立提供 http 服务。可以做网页静态服务器。

虚拟主机。可以实现在一台服务器虚拟出多个网站，例如个人网站使用的虚拟机。

nginz 中也可以配置安全管理、比如可以使用 Nginx 搭建 API 接口网关，对每个接口服务进行拦截。

### 配置文件 nginx.conf

```
worker_processes  1；                			# worker进程的数量
events {                              			# 事件区块开始
    worker_connections  1024；          		# 每个worker进程支持的最大连接数
}                               			    # 事件区块结束
http {                           			    # HTTP区块开始
    include       mime.types；         			# Nginx支持的媒体类型库文件
    default_type  application/octet-stream；            # 默认的媒体类型
    sendfile        on；       				    # 开启高效传输模式
    keepalive_timeout  65；       			    # 连接超时
    server {            		                # 第一个Server区块开始，表示一个独立的虚拟主机站点
        listen       80；      			        # 提供服务的端口，默认80
        server_name  localhost；    			# 提供服务的域名主机名
        location / {            	        	# 第一个location区块开始
            root   html；       			    # 站点的根目录，相当于Nginx的安装目录
            index  index.html index.htm；       	# 默认的首页文件，多个用空格分开
        }          				                    # 第一个location区块结果
        error_page   500502503504  /50x.html；          # 出现对应的http状态码时，使用50x.html回应客户
        location = /50x.html {          	        # location区块开始，访问50x.html
            root   html；      		      	        # 指定对应的站点目录为html
        }
    }  
    ......
```

###

###

---

## 提高部分

---

###

---


---

参考链接：

- [总结最全面的 Nginx 面试题](https://juejin.cn/post/6844904125784653837)
- []()
- []()
- []()
- []()

---



