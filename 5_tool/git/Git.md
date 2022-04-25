# Git

---

## 基础部分

---

### Git 本地回退到之前版本

IDEA：

> reset current branch to here

Git 命令：

> git log
>
> git reset --hard 版本号
>
> git push -f

### Git reset soft 和 hard 区别

reset --hard：**重置暂存区和工作目录**。

reset --soft：保留工作目录，**把重置 HEAD 所带来的新的差异放进暂存区**。

reset 不加参数：mixed，保留工作目录，并清空暂存区。

### reset 和 revert

reset：**回退版本**；

revert：**还原某次或者某几次提交**。


---

## 提高部分

---

###

---


---

参考链接：

- [Git 恢复之前版本的两种方法 reset、revert（图文详解）](https://blog.csdn.net/yxlshk/article/details/79944535)
- [Git Reset 三种模式](https://www.jianshu.com/p/c2ec5f06cf1a)
- []()

---



