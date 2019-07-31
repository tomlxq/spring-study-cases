# 二、嵌入式tomcat

## web技术栈

### servlet传统技术栈

`org.springframework.web.context.ContextLoaderListener`

`org.springframework.context.ApplicationContext`

### web flux(netty)

## Web自动装配

### API角度

Servlet3.0+API实现了Web自动装配`ServletContainerInitializer`

### 容器角度

传统的app部署，是将APP应用部署到servlet容器中

嵌入式app部署，灵活部署，任意指定位置或复杂的条件

tomcat7是servlet 3.0的实现，`ServletContainerInitializer`

tomcat8是servlet 3.1的实现，NIO `HttpServletRequest`、`HttpServletResponse`

> NIO并非一定能够提高性能，比如请求数据量较大，NIO性能比BIO还要差。
>
> NIO多工，读，写，同步的

## jar启动

`publicclass JarFile extends ZipFile` jar和zip文件没啥区别

`mvn -Dmaven.test.skip -U clean package`

`java -jar` 或`jar` 读取`META-INF\MANIFEST.MF`，为引导类`Main-Class`

```
Manifest-Version: 1.0
Main-Class: org.apache.tomcat.maven.runner.Tomcat7RunnerCli
```

> 参考jdk API: `java.util.jar.Manifest`

`java -jar target/tomcat-war-demo-1.0-snapshot-war-exec.jar`

修改端口，可以查看源码`Tomcat7RunnerCli.java`

`java -jar target/tomcat-war-demo-1.0-snapshot-war-exec.jar  -httpPort=7070`

`

## Maven插件

* `tomcat7-maven-plugin`

  查看源码，首选引入到工程

  ```xml
  <dependency>
      <groupId>org.apache.tomcat.maven</groupId>
      <artifactId>tomcat7-maven-plugin</artifactId>
      <version>2.2</version>
  </dependency>
  ```

  调用链路： `org.apache.tomcat.maven.runner.Tomcat7RunnerCli#main`->`org.apache.tomcat.maven.runner.Tomcat7Runner#run`

* `tomcat8-maven-plugin`







API编程

Spring Boot

​		