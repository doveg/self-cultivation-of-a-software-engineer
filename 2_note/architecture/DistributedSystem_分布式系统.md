# 分布式系统

---

## 常见问题

---

### 分布式锁

要素：排他性、容错性。

redisson 是 redis 官方推荐的一个分布式锁的框架。

底层是用 lua 脚本实现，同时又提供了 watchdog(看门狗)机制，在锁将要过期的时候，会自动检测业务是否执行完成，

如果没有完成，则自动延长锁的过期时间，直到业务执行完成。

而且最重要的一点，使用起来非常简单，几行代码就可以搞定，不像原生锁那样繁琐，是我们进行分布式锁开发的不二选择。

### 分布式事务

###

###

###

---

参考链接：

- [浅谈 Scrum](https://zhuanlan.zhihu.com/p/49048186)
- []()
- []()
- []()
- []()

---

