# spring cloud netflix eureka

## 前微服务时代

* 分布式系统的基本组成
  * 服务提供方 Provider
  * 服务消费方 Consumer
  * 服务注册中心 Registry
  * 服务路由 Router
  * 服务代理 Broker
  * 通讯协议 Protocal

## 高可用架构

* 基本原则
  * 消除单点失败
  * 可靠性交迭
  * 故障探测
* 可用性比率计算
  

Eureka客户端

* 服务发现

  * 传统技术

    * WebService

      UDDI

    * REST
      HATEOAS
    * Java
      JMS
      JNDin 

* 服务注册
  * 客户端
    * Netfilx Eureka Client
      * 激活 @EnableEurekaClient
      * 健康指标 HealthIndicator
    
   * 服务端
  
     * Netfilx Eureka Server
  
       @激活 @EnableEurekaServer
  

Eureka服务器

