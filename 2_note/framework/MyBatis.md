# MyBatis

---

## 基础部分

---

### foreach

---

## 提高部分

---

### MyBatis 重要组件

Configuration：MyBatis 所有的配置信息都保存在 Configuration 对象之中，配置文件中的大部分配置都会存储到该类中

SqlSession：作为 MyBatis 工作的主要顶层 API，表示和数据库交互时的会话，完成必要数据库增删改查功能

Executor：MyBatis 执行器，是 MyBatis 调度的核心，负责 SQL 语句的生成和查询缓存的维护

StatementHandler：封装了 JDBC Statement 操作，负责对 JDBC statement 的操作，如设置参数等

ParameterHandler：负责对用户传递的参数转换成 JDBC Statement 所对应的数据类型

ResultSetHandler：负责将 JDBC 返回的 ResultSet 结果集对象转换成 List 类型的集合

TypeHandler：负责 java 数据类型和 jdbc 数据类型 (也可以说是数据表列类型) 之间的映射和转换

MappedStatement：MappedStatement 维护一条 <select|update|delete|insert> 节点的封装

SqlSource：负责根据用户传递的 parameterObject，动态地生成 SQL 语句，将信息封装到 BoundSql 对象中，并返回

BoundSql：表示动态生成的 SQL 语句以及相应的参数信息

### MyBatis 层次结构

![MyBatis层次结构](../../7_image/framework/MyBatis层次结构.bmp)



---



---

参考链接：

- [Mybatis 层次结构与执行流程](https://blog.csdn.net/LIZHONGPING00/article/details/123887586)
- []()
- []()
- []()

---





