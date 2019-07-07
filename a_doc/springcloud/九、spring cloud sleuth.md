# spring cloud sleuth

论文：

Google Dapper

官网：

[spring-cloud-sleuth](https://cloud.spring.io/spring-cloud-sleuth/2.1.x/single/spring-cloud-sleuth.html)

## 基本概念

1. **Span**: The basic unit of work

2. **Trace:** A set of spans forming a tree-like structure.

3. **Annotation:** Used to record the existence of an event in time.

   ![1562472317587](img\sleuth.png)



## zipkin服务器

### 下载`zipkin-server-2.15.0-exec.jar`

`https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.15.0/zipkin-server-2.15.0-exec.jar`

### 运行`zipkin-server-2.15.0-exec.jar`

`java -jar .\zipkin-server-2.15.0-exec.jar`

### 访问zipkin

`http://localhost:9411`

## 配置`sleuth`

### 引入Maven依赖

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```



引入后，日志会发生变化

> slf4j: https://www.slf4j.org/manual.html
>
> MDC: <https://logback.qos.ch/manual/mdc.html>



## Http收集（Http调用）

### 增加maven依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

### 增加zipkin服务器地址

```properties
# 增加zipkin的服务器地址
spring.zipkin.discovery-client-enabled=true
spring.zipkin.enabled=true
spring.zipkin.base-url=http://localhost:30000
```

> 手工用命令输填入值
>
> `curl -XPOST http://localhost:6060/person-client/person/save -H "content-type:application/json;charset=UTF-8" -d '{"id":15,"name":"testit"}'`

### `spring-cloud-zuul`(6060)上报zipkin服务器

#### 增加maven依赖

```xml
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

#### 增加zipkin的服务器地址

```properties
#zipkin的服务器配置
zipkin.server.host=localhost
zipkin.server.port=9411
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}
```

#### 观察traceId

`person-provider`(7070)

`[person-provider,dc03e57a85460fcf,8d1ccc42510971ec,false]`

`person-client`(8080)

` [person-client,dc03e57a85460fcf,46e612997bff12d1,false] `

`sleuth`(20000)

`[spring-cloud-sleuth,dc03e57a85460fcf,dc03e57a85460fcf,false] `

> http://localhost:20000/zuul/person-client/person/findAll
>
> 访问链
>
> sleuth->zuul(config)->person-client(可能会hystrix)->person-provider

## spring cloud stream收集(消息)



### 日志收集（文件系统）

日志+hoodap