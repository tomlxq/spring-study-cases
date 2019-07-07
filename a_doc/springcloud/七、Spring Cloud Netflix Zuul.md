

# Spring Cloud Netflix Zuul

## 整合Zuul

### 启动`spring-cloud-eureka-server`,`person-provider`

### 调用链路

 Zuul->person-provider

配置文件

```properties
spring.application.name=spring-cloud-zuul
server.port=6060
management.endpoints.web.exposure.exclude=*
management.endpoints.web.base-path=/
spring.management.security.enabled=false
# 显示health细节
management.endpoint.health.show-details=always
# ~~~~~~~~~~~~这是本地的调试方式~~~~~~~~~~~~
# ribbon取消eureka整合
ribbon.eureka.enabled=false
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-provider服务调用
zuul.routes.person-provider=/person-provider/**
# 配置person-provider负载均衡器列表
person-provider.ribbon.listOfServers=\
  http://localhost:7070
```



> 注意：http://localhost:6060/person-provider/person/findAll
>
> person-provider： {app-url-prefix}
>
> person/findAll: 是具体person-provider的URL 

## 整合 eureka

### 引入依赖`spring-cloud-starter-netflix-eureka-client`

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 激活服务注册、发现客户端

`@EnableDiscoveryClient`

### 配置服务注册、发现客户端

```properties
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
```

### 配置文件

```properties
spring.application.name=spring-cloud-zuul
server.port=6060
management.endpoints.web.exposure.exclude=*
management.endpoints.web.base-path=/
spring.management.security.enabled=false
# 显示health细节
management.endpoint.health.show-details=always
# ~~~~~~~~~~~~整合Eureka~~~~~~~~~~~~
#配置服务发现，注册到eureka
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
```

## 整合hystrix

### 引入依赖

在`person-provider`

```xml
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

### 激活hystrix

```java
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class PersonProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonProviderApp.class, args);
    }
}
```

### 配置fallback

`HystrixPropertiesManager`

```java
/**
     * Command execution properties.
     */
public static final String EXECUTION_ISOLATION_STRATEGY = "execution.isolation.strategy";
public static final String EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS = "execution.isolation.thread.timeoutInMilliseconds";
public static final String EXECUTION_TIMEOUT_ENABLED = "execution.timeout.enabled";
public static final String EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT = "execution.isolation.thread.interruptOnTimeout";
public static final String EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS = "execution.isolation.semaphore.maxConcurrentRequests";
```

### 增加容断时的处理方法

```java
@RestController
@Slf4j
public class PersonController {
    private final Map<Long, Person> map = new ConcurrentHashMap();

    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person) {
        return map.put(person.getId(), person) == null;
    }

    private final Random random = new Random();

    /**
     * 超过100ms服务熔断
     * 
     * @return
     */
    @GetMapping("/person/findAll")
    @HystrixCommand(fallbackMethod = "fallbackFindAll", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")})

    Collection<Person> findAll() throws InterruptedException {
        int time = random.nextInt(200);
        log.info("findAll cost {} ms。 {}", time, Thread.currentThread().getName());
        Thread.sleep(time);
        return map.values();
    }

    Collection<Person> fallbackFindAll() {
        log.error("发生服务熔断，调到fallbackFindAll. {}", Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
```

## 整合Feign

服务消费端`person-client`

### 调整用链路

`zuul(6060)`->`person-client`(8080)->``person-provider`(7070)

`Eureka server(9090)`

### 配置

```properties
spring.application.name=person-client
server.port=8080
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
spring.management.security.enabled=false
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
```

### 网关应用 `spring-cloud-zuul`

增加路由应用到`person-client`

```properties
spring.application.name=spring-cloud-zuul
server.port=6060
management.endpoints.web.exposure.exclude=*
management.endpoints.web.base-path=/
spring.management.security.enabled=false
# 显示health细节
management.endpoint.health.show-details=always
# ~~~~~~~~~~~~整合Eureka~~~~~~~~~~~~
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-provider\person-client服务调用
zuul.routes.person-provider=/person-provider/**
zuul.routes.person-client=/person-client/**
#配置服务发现，注册到eureka
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
```

等价ribbon不走注册中心

```properties
# ribbon取消eureka整合
ribbon.eureka.enabled=false
# 配置person-provider负载均衡器列表
person-provider.ribbon.listOfServers=http://localhost:7070
person-client.ribbon.listOfServers=http://localhost:8080
```

### 调用测试

查询 `localhost:6060/person-client/person/findAll`

保存`localhost:6060/person-client/person/save`

报文

```json
{
	"id":"4",
    "name": "tom",
    "password":"123455"
}
```

## 整合config server

### 各应用的端口

`Zuul` 6060 (nginx)

`Person-client` 8080 (dubbo consumer)

`Person-provider` 7070 (dubbo provider)

`Eureka server` 9090 (dubbo)

`Config server` 10000 (zookeeper)

### config server的配置文件

```properties
spring.application.name=spring-cloud-config
server.port=10000
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
#从读取远程github配置文件####################################################
spring.cloud.config.server.git.uri=https://github.com/tomlxq/gs-spring-cloud-config.git
spring.cloud.config.server.git.search-paths=respo
spring.cloud.config.label=master
#spring.cloud.config.server.git.uri=\
#  file:///${user.dir}/src/main/resources/config
spring.management.security.enabled=false
management.endpoints.web.base-path=/
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
```

在gs-spring-cloud-config.git下增加respo，并建几个配置文件

`zuul.properties`

```properties
# 应用spring-cloud-zuul默认配置项（profile为空）
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-provider服务调用
zuul.routes.person-provider=/person-provider/**
```

`zuul-test.properties`

```properties
# 应用spring-cloud-zuul默认配置项（profile为test）
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-client服务调用
zuul.routes.person-client=/person-client/**
```

`zuul-prod.properties`

```properties
# 应用spring-cloud-zuul默认配置项（profile为prod）
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-provider服务调用
zuul.routes.person-provider=/person-provider/**
# zuul配置person-client服务调用
zuul.routes.person-client=/person-client/**
```



### 将服务注册到配置中心

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 激活服务发现，服务注册到Eureka

```java
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class GsSpringCloudConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudConfigApplication.class, args);
    }
}
```

### 测试配置

`http://localhost:10000/zuul/default`

`http://localhost:10000/zuul/test`

`http://localhost:10000/zuul/prod`

### 调整网关服务`spring-cloud-zuul`

#### 注掉之前的配置`zuul`配置`person-provider`、`person-client`

```properties
spring.application.name=spring-cloud-zuul
server.port=6060
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
spring.management.security.enabled=false
# 显示health细节
management.endpoint.health.show-details=always
# ~~~~~~~~~~~~这是本地的调试方式~~~~~~~~~~~~
# ribbon取消eureka整合
#ribbon.eureka.enabled=false
# 配置person-provider负载均衡器列表
#person-provider.ribbon.listOfServers=http://localhost:7070
#person-client.ribbon.listOfServers=http://localhost:8080
# ~~~~~~~~~~~~整合Eureka~~~~~~~~~~~~
# zuul的基本配置模式
# zuul.routes.${app-name}=${app-url-prefix}/**
# zuul配置person-provider服务调用
#zuul.routes.person-provider=/person-provider/**
# zuul配置person-client服务调用
#zuul.routes.person-client=/person-client/**

```



#### 增加`spring-cloud-config-server`依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
#### 增加配置文件`bootstrap.properties`
```properties
# bootstrap上下文件设置
# 配置服器URI
spring.cloud.config.uri=http://localhost:9090/
# 配置客户端应用名称
spring.cloud.config.name=zuul
# profile是激活配置
spring.cloud.config.profile=prod
# label在GIT中指的分支名称
spring.cloud.config.label=master
# 采用discovery client的连接方式
# https://cloud.spring.io/spring-cloud-config/2.1.x/single/spring-cloud-config.html
spring.cloud.config.discovery.enabled=true
# 配置配置服务器的应用名称，对应配置服务器spring.application.name={spring-cloud-config}
spring.cloud.config.discovery.serviceId=spring-cloud-config
```

#### 如何查找`spring.cloud.config.uri`默认配置

1. 打开搜索，选`Scope`->`Project and Libraries`

![1562342776316](img\findconfig.png)

2. 找`META-INF/spring-configuration-metadata.json`点击`ConfigClientProperties`

```json
{
    "name": "spring.cloud.config.discovery",
    "type": "org.springframework.cloud.config.client.ConfigClientProperties$Discovery",
    "sourceType": "org.springframework.cloud.config.client.ConfigClientProperties",
    "sourceMethod": "getDiscovery()"
}
```

3. 搜`uri`

```java
/**
	 * The URI of the remote server (default http://localhost:8888).
	 */
private String[] uri = { "http://localhost:8888" };
```

#### 检查是否从配置中心读取了服务配置

`http://localhost:6060/env`

```json
{
name: "configService:https://github.com/tomlxq/gs-spring-cloud-config.git/respo/zuul-prod.properties",
properties: {
zuul.routes.person-provider: {
value: "/person-provider/**"
},
zuul.routes.person-client: {
value: "/person-client/**"
}
}
}
```



#### 检查调用链路是否生效

URL `localhost:6060/person-client/person/save`

```json
{
	"id":"1",
    "name": "tom",
    "password":"123455"
}
```

URL `localhost:6060/person-client/person/findAll`

```json
[
    {
        "id": 1,
        "name": "tom"
    },
    {
        "id": 2,
        "name": "tom"
    },
]
```



主线程`RequestContext`持有`ThreadLocal<? extends RequestContext> threadLocal`

```java
protected static final ThreadLocal<? extends RequestContext> threadLocal = new ThreadLocal<RequestContext>() {
    @Override
    protected RequestContext initialValue() {
        try {
            return contextClass.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
};
```



`ContextLifecycleFilter`

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {
    try {
        chain.doFilter(req, res);
    } finally {
        RequestContext.getCurrentContext().unset();//采用了线程程，线程完成后，GC没有清掉，强制remove
    }
}
```

`ZuulServlet`也会清除掉`RequestContext`

```
} finally {
 RequestContext.getCurrentContext().unset();
}
```

`ZuulServletFilter`

```
} finally {
RequestContext.getCurrentContext().unset();
}
```