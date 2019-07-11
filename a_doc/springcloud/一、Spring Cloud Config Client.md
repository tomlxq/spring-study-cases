# 一、Spring Cloud Config Client

技术回顾 

回顾提及的Environment以及Spring Boot配置相关的事件和监听器，如`ApplicationEnvironmentPreparedEvent`以及`ConfigFileApplicationListener`

Bootstrap配置属性

解密Bootstrap配置属性与Spring Framework、Spring Boots配置架构的关系，介绍如何调整Bootstrap配置文件路径，覆盖过程配置属性，自定义Bootstrap配置以及自定义Bootstrap配置属性源

Environment端点

介绍/env端点的使用场景，并且解读其源代码，了解其中的奥秘

安全

介绍客户端配置安全相关议题





## Spring Cloud技术体系

- 前提基础

  构建系统： Maven、Gradle

  基础框架：Spring Framework、Spring Boot

- 功能

  分布式配置：Distributed/versioned configuration   zookeeper

  服务注册和发现：Service registration and discovery  dubbo

  路由：Routing

  服务调用：Service-to-Service call

  负载均衡：Load balancing

  短路：Circuit Breakers

  分布式消息：Distributed Messages

  

## Spring/Spring Boot事件机制

设计模式

- 观察者模式

  ```
  发布者 java.util.Observable
  订阅者 java.util.Observer 
  ```

- 事件、监听器模式

  `java.util.EventObject` 事件对象

  - 事件对易用总是关联着事件源 `java.util.EventObject#source`

  `java.util.EventListener` 事件监听接口（标记）

Spring 核心接口

- ApplicationEvent

  - org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
  - org.springframework.boot.context.event.ApplicationPreparedEvent
  - org.springframework.boot.context.event.ApplicationStartedEvent
  - org.springframework.boot.context.event.ApplicationReadyEvent
  - org.springframework.boot.context.event.ApplicationFailedEvent

- ApplicationListener

  - ConfigFileApplicationListener

    管理配置文件，比如`application.properties`以及`application.yaml`

    加载的优先顺序

    profile=dev、test

    application-{profile}.properties

    application.properties

    - Java SPI:  `java.util.ServiceLoader`

    - Spring SPI:

      Spring Boot在相对于classes:  `/META-INF/spring.factories`

      ```xml
      # Application Listeners
      org.springframework.context.ApplicationListener=\
      org.springframework.boot.ClearCachesApplicationListener,\
      org.springframework.boot.builder.ParentContextCloserApplicationListener,\
      org.springframework.boot.context.FileEncodingApplicationListener,\
      org.springframework.boot.context.config.AnsiOutputApplicationListener,\
      org.springframework.boot.context.config.ConfigFileApplicationListener,\
      org.springframework.boot.context.config.DelegatingApplicationListener,\
      org.springframework.boot.context.logging.ClasspathLoggingApplicationListener,\
      org.springframework.boot.context.logging.LoggingApplicationListener,\
      org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener
      ```

      如何控制顺序?

      实现Ordered或@Order `org.springframework.boot.context.config.ConfigFileApplicationListener#order`

      在spring里数字越小越优先

- spring cloud 加载事件BootstrapApplicationListener、LoggingSystemShutdownListener

  ```
  org.springframework.context.ApplicationListener=\
  org.springframework.cloud.bootstrap.BootstrapApplicationListener,\
  org.springframework.cloud.bootstrap.LoggingSystemShutdownListener,\
  org.springframework.cloud.context.restart.RestartListener
  ```

  `BootstrapApplicationListener`

  - `BootstrapApplicationListener`与`ConfigFileApplicationListener`加载估先级

    加载的优先级高于ConfigFileApplicationListener,所以application.properties文件即使定义了也取不到。

  ​       在application.propertis

  ```xml 
  spring.cloud.bootstrap.name=abc
  ```

  ![1560730270944](E:/data/wwwtest/spring-study-cases/a_doc/springboot/img/config_bootstrap_name.png)

  

  

  - 负责加载`bootstrap.properties `或`bootstrap.yaml`

  - 负责初始化Bootstrap Application Context ID="bootstrap"

    ```java
    ConfigurableApplicationContext context = builder.run(new String[0]);
    context.setId("bootstrap");
    ```



## Bootstrap配置属性

- Bootstrap配置文件路径

  - ```properties
    spring.cloud.bootstrap.location
    ```

- 覆盖远程配置属性

  - ```properties
    spring.cloud.config.allow-override=true
    ```



## 理解Environment端点

- Spring Boot Actuator
  - endpoint: /env
- Spring Framework
  - Environment API



