#基础镜像
FROM openjdk:8-jdk-alpine

#设置环境变量
ENV DOCKERIZE_VERSION v0.6.1

#执行命令
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

#执行命令，支持 shell 和 exec 两种格式
RUN apk add --no-cache tzdata ttf-dejavu

#数据卷
VOLUME /opt/jar

#暴露端口
EXPOSE 8080

#健康检查
HEALTHCHECK --interval=30s --start-period=60s \
  CMD wget http://localhost:8080 -qO /dev/null || exit 1

#工作目录
WORKDIR /opt/jar
#运行容器
CMD java -Xms256m -Xmx1024m -Xss256K -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -jar /opt/jar/springboot.jar --spring.profiles.active=development

