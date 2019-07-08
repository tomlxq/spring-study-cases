# spring cloud sleuth

论文：

Google Dapper

官网：

[spring-cloud-sleuth](https://cloud.spring.io/spring-cloud-sleuth/2.1.x/single/spring-cloud-sleuth.html)

## 基本概念

sleuth 问题排查，调用链路跟踪，性能问题

> 了解一下opentsdb

1. **Span**: The basic unit of work

2. **Trace:** A set of spans forming a tree-like structure.

3. **Annotation:** Used to record the existence of an event in time.

   ![1562472317587](img\sleuth.png)

通过trance Id查找链路，通过span Id 定位环节

## zipkin服务器

https://zipkin.io/pages/quickstart.html

> zipkin用来收集上报的日志信息

### 下载`zipkin-server-2.15.0-exec.jar`

`https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.15.0/zipkin-server-2.15.0-exec.jar`

### 运行`zipkin-server-2.15.0-exec.jar`

`java -jar .\zipkin-server-2.15.0-exec.jar`

### 访问zipkin

`http://localhost:9411`

## 创建zipkin服务器

#### 增加maven依赖

```xml
 <dependency>
     <groupId>io.zipkin.java</groupId>
     <artifactId>zipkin-server</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-autoconfigure-ui</artifactId>
    <scope>runtime</scope>
</dependency>
```

#### 激活zipkin服务器

```java
@SpringBootApplication
@EnableZipkinServer
public class GsSpringCloudZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudZipkinApplication.class, args);
    }
}
```



## Http收集（Http调用）

### 整合`spring-cloud-sleuth`

#### 引入Maven依赖

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

引入后，日志会发生变化

> slf4j: https://www.slf4j.org/manual.html
>
> MDC: <https://logback.qos.ch/manual/mdc.html>

![1562599538895](img\big_sleuth.png)

> 启动服务
>
> 1. `spring-cloud-sleuth` 20000
> 2. `spring-cloud-eureka-server` 9090
> 3. `spring-cloud-config` 10000
> 4. `person-provider` 7070
> 5. `person-client` 8080
> 6. `spring-cloud-zuul` 6060

#### 激活Eureka

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GsSpringCloudSleuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudSleuthApplication.class, args);
    }
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

#### 增加zipkin服务器地址

```properties
spring.application.name=spring-cloud-sleuth
server.port=20000
# 安全失效
spring.management.security.enabled=false
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
# 注册到eureka服务器
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
# 增加zipkin的服务器地址
spring.zipkin.discovery-client-enabled=true
spring.zipkin.enabled=true
#zipkin的服务器配置
zipkin.server.host=localhost
zipkin.server.port=9411
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}
```

> 手工用命令输填入值
>
> `curl -XPOST http://localhost:6060/person-client/person/save -H "content-type:application/json;charset=UTF-8" -d '{"id":15,"name":"testit"}'`

#### 调整`spring-cloud-sleuth`代码

```java
@RestController
@Slf4j
public class DemoController {
    @Autowired
    public DemoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    @GetMapping("/demo")
    public String index() {
        String message = "Hello, Tom";
        log.info("{} message {}", getClass().getName(), message);
        return message;
    }

    /**
     * http://localhost:20000/zuul/person-client/person/findAll
     * 调用链路 sleuth(20000)->zuul(6060)->person-client(8080)->person-provider(7070)
     * 
     * @return
     */
    @GetMapping("/zuul/person-client/person/findAll")
    public Object toZuul() {
        log.info("DemoController#toZuul()");
        String url = "http://spring-cloud-zuul/person-client/person/findAll";
        return restTemplate.getForObject(url, Object.class);
    }
}
```

#### 调整zuul、client、provider上报zipkin服务器

##### 增加maven依赖

```xml
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

##### 增加zipkin的服务器地址

```properties
#zipkin的服务器配置
zipkin.server.host=localhost
zipkin.server.port=9411
spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}
```

##### 观察traceId

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

### 调整`spring-cloud-zipkin`,通过`stream`来收集

> `mvn -Dmaven.test.skip -U clean package`

##### 增加maven依赖

```xml
<!-- zipkip服务器通过stream收集跟踪信息-->
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-sleuth-zipkin-stream</artifactId>
</dependency>
<!-- 使用kafka作为stream服务器-->
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

##### 激活stream

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableZipkinStreamServer
public class GsSpringCloudZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudZipkinApplication.class, args);
    }
}
```

##### 其它需要通过`kafka`上报，增加依赖

```xml
<!-- zipkip服务器通过stream收集跟踪信息-->
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-sleuth-zipkin-stream</artifactId>
</dependency>
<!-- 使用kafka作为stream服务器-->
<dependency>
 <groupId>org.springframework.cloud</groupId>
 <artifactId>spring-cloud-stream-binder-kafka</artifactId>
</dependency>
```

> 把之前上报Http URL 注释掉：
>
> ```xml
> #zipkin的服务器配置
> zipkin.server.host=localhost
> zipkin.server.port=9411
> spring.zipkin.base-url=http://${zipkin.server.host}:${zipkin.server.port}
> ```





### 日志收集（文件系统）

日志+hoodap