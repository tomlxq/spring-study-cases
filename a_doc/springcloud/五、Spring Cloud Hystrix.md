# 五、Spring Cloud Hystrix

## 核心理念

介绍服务短路的名词由来，目的以及相关的类似概念。其中的设计哲学，触发条件，处理手段以及客户端和服务器的实现方法

## Spring Cloud Hystrix

作为服务器端的服务短路实现，介绍了Spring Cloud Hystrix常用限流功能，同时说明健康指标以及数据指标在生产环境下的现实意义

## 生产准备特性

介绍聚合数据指标Turbine、Turbine Stream以及整合Hystrix Dashboard

官方： <https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.1.2.RELEASE/single/spring-cloud-netflix.html>

增加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-netflix-turbine</artifactId>
</dependency>
```

激活turbine

`@EnableTurbine`

管理端口为8081

```properties
eureka.instance.metadata-map.management.port=${management.port:8081}
turbine.aggregator.clusterConfig=CUSTOMERS
turbine.appConfig=customers
```

Dashboard输入，显示监控

`http://localhost:8080/turbine.stream?cluster=CUSTOMERS`



