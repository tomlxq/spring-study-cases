# Spring cloud netflix hystrix

> https://github.com/Netflix/Hystrix
>
> 容错，短路
>
> https://martinfowler.com/bliki/CircuitBreaker.html 
>
> QPS Query per second
>
> 经过全链路(一个完整的业务流程)压测，计算单机极限QPS 
>
> `集群QPS=单机QPS*集群机器数量*可靠性比率`
>
> TPS Transaction per second
>
>  JMeter 可调整性比较灵活

## 服务短路（circuitBreaker)

## Spring client Hystrix Client

官网： https://github.com/Netflix/Hystrix

> Reactive Java 框架 
>
> https://reactive.io/
>
> * Java 9 Flow API
> * Reactor
> * RxJava （Reactive X）
>   ExecutorService、CompletionService、CompletableFuture

配置信息的WIKI ： <https://github.com/Netflix/Hystrix/wiki/Configuration>

Spring Client Hystrix Dashboard

整合Netflix Turbine