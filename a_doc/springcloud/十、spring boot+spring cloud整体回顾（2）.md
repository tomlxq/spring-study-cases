# spring boot+spring cloud整体回顾（2）

* 监控信息埋点`sleuth`

  * Http上报

    sleuth+zipkin

  * Spring cloud stream上报

    Rabbit MQ Binder

    Kafka Binder

* 监控收集`zipkin`

  - Http方式 

    sleuth+zipkin

  - Spring cloud stream

    Rabbit MQ Binder

    Kafka Binder

  - 日志收集

    ELK 

    日志格式调整，从普通单行日志，变成JSON格式（Base on ElasticSearch)

* 限流Hystrix

  `@HystrixCommand`

  `HystrixCommand`

  管理平台：hystrix+dashboard

  数据聚合：Turbine

* 分布式配置

  * spring cloud config 客户端

    直接连接方式

    利用Discovery Client

  * spring cloud config 服务端

    利用Discovery Client让其它客户发现服务器

    * Git Base 实现
    * Consul实现 
    * Zookeeper实现

  > java -jar -Xmx xxx.jar

