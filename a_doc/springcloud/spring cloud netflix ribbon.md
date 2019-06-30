# spring cloud netflix ribbon

## Eureka服务端的高可用

* 高可用注册中心集群
  * 增加Eureka注册地址

    ```properties
    eureka.client.serviceUrl.defaultZone=\  http://localhost:9090/eureka,http://localhost:9091/eureka
    ```

    配置源码`EurekaClientConfigBean`

    配置项`eureka.client.serviceUrl`实际映射字段为`serviceUrl`,是Map类型，Key为自定议，默认值为`defaultZone`,value是需要配置的Eureka注册服务器URL。

    ```java
    private Map<String, String> serviceUrl = new HashMap<>();
    {
        this.serviceUrl.put(DEFAULT_ZONE, DEFAULT_URL);
    }
    ```

    value可以是多值字段，可以通过逗号来分隔

    ```java
    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        String serviceUrls = this.serviceUrl.get(myZone);
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            serviceUrls = this.serviceUrl.get(DEFAULT_ZONE);
        }
        if (!StringUtils.isEmpty(serviceUrls)) {
            final String[] serviceUrlsSplit = StringUtils
                .commaDelimitedListToStringArray(serviceUrls);
            List<String> eurekaServiceUrls = new ArrayList<>(serviceUrlsSplit.length);
            for (String eurekaServiceUrl : serviceUrlsSplit) {
                if (!endsWithSlash(eurekaServiceUrl)) {
                    eurekaServiceUrl += "/";
                }
                eurekaServiceUrls.add(eurekaServiceUrl.trim());
            }
            return eurekaServiceUrls;
        }

        return new ArrayList<>();
    }
    ```
  
  * 获取注册信息时间间隔

    Eureka客户端要获取Eureka服务器注册信息，这个 方便服务调用。

    Eureka客户端`EurekaClient`关联多个应用`Applications`

    每个`Application`关联多个`InstanceInfo`

    当应用调用某个服务时，实际上是对应对象Application,关联了许多应用实例InstanceInfo

    当应用实例发现变化时，调用方是需要感知的，感知时间可以通过配置项来调整。

    ```java
    # 调整注册信息的获取周期
    eureka.client.registry-fetch-interval-seconds=30
    ```
    
  * 实例信息复制时间间隔
  
    客户端信息上报到Eureka服务器的时间
  
    ```java
    private int instanceInfoReplicationIntervalSeconds = 30;
    ```
  
    
  
  * 实例ID
  
    从Eureka Server Dashboard里面可以看到具体某个应用中的实例信息，比如：
  
    ```xml
    UP (1) - LAPTOP-RJPNUI7H.mshome.net:user-service-provider:7072
    ```
  
    命名模式：${hostname}:${spring.application.name}:${server.port}
  
    具体配置可以查看`EurekaInstanceConfigBean`
  
    例如：
  
    ```properties
    # 修改eureka应用实例名称
    eureka.instance.instance-id=${spring.application.name}:${server.port}
    ```
  
    
    
  * 实例端点映射
  
      源码：`EurekaInstanceConfigBean.java`
      
      ```properties
      # 修改eureka客户端status的URL
      eureka.instance.status-page-url-path=/health
      ```



## Eureka客户端高可用

开启Eureka相互注册

 `application-peer1.properties`

```properties
# Eureka Server的应用名称
spring.application.name=spring-cloud-eureka-server
# Eureka Server的服务端口
server.port=9090
# 取消服务器自我注册
eureka.client.register-with-eureka=true
# 不需要检索服务
eureka.client.fetch-registry=true
# Eureka Server的服务URL，用于向另一台Eureka服务器复制数据
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9091/eureka
```

`application-peer2.properties`

```properties
# Eureka Server的应用名称
spring.application.name=spring-cloud-eureka-server
# Eureka Server的服务端口
server.port=9091
# 取消服务器自我注册
eureka.client.register-with-eureka=true
# 不需要检索服务
eureka.client.fetch-registry=true
# Eureka Server的服务URL，用于向另一台Eureka服务器复制数据
eureka.client.serviceUrl.defaultZone=\
  http://localhost:9090/eureka
```

通过`--spring.profiles.active=peer1`和`--spring.profiles.active=peer2`分别激活Eureka Server1和Eureka Server2。



## Spring RestTemplate

### HTTP消息装换器： HttpMessageConverter

自定义实现

编码问题

```java
RestTemplate restTemplate = new RestTemplate();
System.out.println(restTemplate.getForObject("http://localhost:6060/env", Map.class));
System.out.println(restTemplate.getForObject("http://localhost:6060/env", String.class));
```

切换序列化和反序化协议

### HTTP Client适配工厂：ClientHttpRequestFactory

* Spring实现

  ```java
  SimpleClientHttpRequestFactory
  ```

* HttpClient

  ```java
  HttpComponentsClientHttpRequestFactory
  ```

* OkHttp

  ```java
  OkHttp3ClientHttpRequestFactory
  ```

  举例说明：

  ```java
    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
  ```

  切换Http通讯实现，提升性能

### HTTP Client拦截器：ClientHttpRequestInterceptor

```java
 LoadBalancerInterceptor implements ClientHttpRequestInterceptor 
```

RestTemplate增加一个`LoadBalancerInterceptor`,调用Netflix中的RibbonLoadBalancerClient的LoadBalander实现，根据Eureka客户端应用获取目标应用(IP+Port),轮训的方式调用。

```java
server = new Server(instance.getScheme(), instance.getHost(),instance.getPort());
```

实际请求客户端：

* `LoadBalancerClient`

* `RibbonLoadBalancerClient`

referece:

https://www.consul.io/

http://opentsdb.net