

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

