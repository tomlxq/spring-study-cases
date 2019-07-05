# 六、Spring Cloud Feign

## 技术回顾 

Netflix Eureka

RestTemplate

Netflix Ribbon

Netflix Hystrix

> 注：Hystrix可以是服务端实现，也可以是客户端实现。类似AOP封装，一种是正常逻辑，一种是异常逻辑。

## 申请明式Web服务客户端：Feign

申明式：接口声明，Annotation驱动

Web服务：Http方式作为通讯协议

客户端：用于服务调用的存根

Feign，原生并不是Spring Web MVC的实现，基于JAX-RX（Java RESTaurant规范）实现。Spring Cloud封装了Feign，使其支持Spring Web MVC。

需要的服务组件：

1. 注册中心Eureka Server：服务发现和注册

2. Feign客户端：调用Feign申明接口

3. Feign服务端：不一定强制实现Feign申明接口

4. Feign声明接口（契约）：定义一种Java强类型接口

### 引包Jar包

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 定义服务接口

`gs-spring-cloud-feign/person-api/src/main/java/com/example/service/PersonService.java`

```java
@FeignClient(value = "person-provider")
public interface PersonService {
    /**
     * savePerson
     * 
     * @param person
     * @return
     */
    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person);

    /**
     * findAll
     * 
     * @return
     */
    @GetMapping("/person/findAll")
    Collection<Person> findAll();
}
```

### 实现服务接口

`gs-spring-cloud-feign/person-provider/src/main/java/com/example/controller/PersonController.java`

```java
@RestController
public class PersonController {
    Map<Long, Person> map = new ConcurrentHashMap();

    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person) {
        return map.put(person.getId(), person) == null;
    }

    @GetMapping("/person/findAll")
    Collection<Person> findAll() {
        return map.values();
    }
}
```

### 服务提供的启动类

`gs-spring-cloud-feign/person-provider/src/main/java/com/example/PersonProviderApp.java`

```java
@SpringBootApplication
@EnableEurekaClient
public class PersonProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonProviderApp.class, args);
    }
}
```

### 服务提供的配置文件

`gs-spring-cloud-feign/person-provider/src/main/resources/application.properties`

```properties
# 服务名称需要与@FeignClient对应
spring.application.name=person-provider
server.port=7070
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
spring.management.security.enabled=false
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
```

### 编写客户端调用

`gs-spring-cloud-feign/person-client/src/main/java/com/example/controller/PersonController.java`

```java
@RestController
public class PersonController implements PersonService {
    public final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean savePerson(Person person) {
        return personService.savePerson(person);
    }

    @Override
    public Collection<Person> findAll() {
        return personService.findAll();
    }
}
```

### 客户端的启动类

`gs-spring-cloud-feign/person-client/src/main/java/com/example/PersonClientApp.java`

```java
@SpringBootApplication
@EnableFeignClients(clients = PersonService.class)
@EnableEurekaClient
public class PersonClientApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonClientApp.class, args);
    }
}
```

### 客户端的配置文件

`gs-spring-cloud-feign/person-client/src/main/resources/application.properties`

```properties
spring.application.name=person-client
server.port=8080
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
spring.management.security.enabled=false
management.endpoints.web.exposure.include=*
management.endpoints.web.base-path=/
```

postman -> person-client -> person-provider



## 整合Netflix Ribbon

官方参考文档：

<https://cloud.spring.io/spring-cloud-static/Greenwich.SR2/single/spring-cloud.html#spring-cloud-ribbon-without-eureka>

### 调整person-client关闭eureka

```properties
#ribbon不使用eureka
ribbon.eureka.enabled=false
```

### 定义服务ribbon的服务列表`person-provider`

```properties
# 定义服务ribbon的负载均衡服务列表`person-provider`
person-provider.ribbon.listOfServers=\
  http://localhost:7070,http://localhost:7070,http://localhost:7070
```

### 完全取消Eureka注册

```java
@SpringBootApplication
@EnableFeignClients(clients = PersonService.class)
// @EnableEurekaClient
public class PersonClientApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonClientApp.class, args);
    }
}
```

### 自定义Ribbon规则

* IRule
  * 随机规则：RandomRule
  
  * 最可用规则：BestAvailableRule
  
  * 轮训规则：RoundRobinRule
  
  * 重试规则：RetryRule
  
  * 客户端配置规则：ClientConfigEnabledRoundRobinRule
  
  * 可用性过滤规则：AvailabilityFilteringRule
  
  * RT权重规则：WeightedResponseTimeRule
  
  * 规避区域规则：ZoneAvoidanceRule
  

#### 实现IRule

```java
public class FirstServerForeverServer extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    /**
     * 永远返回第一个服务器
     * 
     * @param key
     * @return
     */
    @Override
    public Server choose(Object key) {
        List<Server> serverList = getLoadBalancer().getAllServers();
        return serverList.get(0);
    }
}
```

#### 暴露自定义实现为SpringBean

```java
/**
     * 暴露{@link FirstServerForeverServer}
     *
     * @return
     */
@Bean
FirstServerForeverServer firstServerForeverServer() {
    return new FirstServerForeverServer();
}
```

#### 激活`@RibbonClient`

```java
@SpringBootApplication
@EnableFeignClients(clients = PersonService.class)
//@EnableEurekaClient
@RibbonClient(value = "person-provider", configuration = PersonClientApp.class)
public class PersonClientApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonClientApp.class, args);
    }

    /**
     * 暴露{@link FirstServerForeverServer}
     *
     * @return
     */
    @Bean
    FirstServerForeverServer firstServerForeverServer() {
        return new FirstServerForeverServer();
    }
}
```



修改服务方为随机端口，再次还原Eureka注册可以看到注册结果

```properties
server.port=${random.int[7070,7079]}
```

注册信息

```reStructuredText
PERSON-PROVIDER	n/a (3)	(3)	UP (3) - LAPTOP-RJPNUI7H.mshome.net:person-provider:7073 , LAPTOP-RJPNUI7H.mshome.net:person-provider:7076 , LAPTOP-RJPNUI7H.mshome.net:person-provider:7071
```



## 整合Netflix Hystrix

引入jar

```properties
  <dependency>
 	 <groupId>org.springframework.cloud</groupId>
  	 <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
  </dependency>
```

修改Feign服务接口

```java
@FeignClient(value = "person-provider", fallback = PersonProviderFailBack.class)
// @RibbonClient(value = "person-provider")
public interface PersonService {
    /**
     * savePerson
     * 
     * @param person
     * @return
     */
    @PostMapping("/person/save")
    boolean savePerson(@RequestBody Person person);

    /**
     * findAll
     * 
     * @return
     */
    @GetMapping("/person/findAll")
    Collection<Person> findAll();

}
```

添加fallback

```java
@Slf4j
public class PersonProviderFailBack implements PersonService {

    @Override
    public boolean savePerson(Person person) {
        log.info("failed: save person failed");
        return false;
    }

    @Override
    public Collection<Person> findAll() {
        log.info("failed: findAll empty");
        return Collections.EMPTY_LIST;
    }
}
```

调整客户端，激活`@EnableHystrix`

```java
@SpringBootApplication
@EnableFeignClients(clients = PersonService.class)
@EnableEurekaClient
@EnableHystrix
// @RibbonClient(value = "person-provider", configuration = PersonClientApp.class)
public class PersonClientApp {
    public static void main(String[] args) {
        SpringApplication.run(PersonClientApp.class, args);
    }

    /**
     * 暴露{@link FirstServerForeverServer}
     *
     * @return
     */
    @Bean
    FirstServerForeverServer firstServerForeverServer() {
        return new FirstServerForeverServer();
    }
}
```

