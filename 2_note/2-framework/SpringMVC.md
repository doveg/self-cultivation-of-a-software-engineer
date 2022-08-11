# SpringMVC

---

## 基础部分

---

### SpringMVC 工作原理

1、客户端（浏览器）发送请求，直接请求到 **DispatcherServlet**。

2、DispatcherServlet 根据请求信息调用 **HandlerMapping**，解析请求对应的 Handler。

3、解析到对应的 Handler（也就是我们平常说的 Controller 控制器）后，开始由 **HandlerAdapter** 适配器处理。

4、HandlerAdapter 会根据 Handler 来调用真正的处理器开处理请求，并处理相应的业务逻辑。

5、处理器处理完业务后，会返回一个 **ModelAndView** 对象，Model 是返回的数据对象，View 是个逻辑上的 View。

6、**ViewResolver** 会根据逻辑 View 查找实际的 View。

7、**DispatcherServlet** 把返回的 Model 传给 View（视图渲染）。

8、把 View 返回给请求者（浏览器）

### SpringMVC 和 Struts2 的区别

**入口的不同：**

Struts2 是 filter

SpringMVC 的入口是 servlet

**拦截器机制：**

Struts2 有以自己的 interceptor 机制。

SpringMVC 用的是独立的 AOP 方式。

**开发效率：**

SpringMVC 可以认为已经零配置

### servlet

Servlet：Java 用来处理网络请求的 API 接口。

Web 服务器，比如 Tomcat，实现 Servlet 的 API 接口，就能处理 HTTP 请求。


---

## 提高部分

---

###

---



---

参考链接：

- [SpringMVC 工作原理详解 ](https://www.cnblogs.com/yoci/p/10642493.html)
- []()

---



