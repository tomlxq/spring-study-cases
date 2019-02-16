点对点注册
````
<dubbo:registry address="N/A"/>
<!--当前服务所依赖的协议：webservice,thrift,hessian,http-->
<dubbo:protocol name="dubbo" port="2181"/>
<!--生成一个远程服务的调用代理-->
<dubbo:reference interface="com.tom.IOrderService" id="orderServices"
url="dubbo://192.168.238.2:20880/com.tom.IOrderService"/>
```` 
用zookeeper来注册服务
````
<dubbo:registry address="zookeeper://192.168.238.105:2181?backup=192.168.238.110:2181,192.168.238.115:2181,192.168.238.120:2181"/>
sh /opt/zookeeper-3.4.10/bin/zkCli.sh 
# 查看注册上来的URL
ls /dubbo/com.tom.IOrderService/providers
````

### admin控制台的安装

https://github.com/apache/incubator-dubbo-ops
【Dubbo】dubbo 2.6.x 的dubbo-admin管理平台的搭建

> dubbo 2.6.x 的 dubbo-admin 管理平台已经经过重构， 改成了使用springboot实现了， 之前下载源码后使用mvn clean package -Dmaven.test.skip=true编译打包后会得到一个war包， 将这个war包扔到Tomcat应用目录下， 配置好ZK后，启动Tomcat就安装成功了。而现在改成了springboot实现之后，安装方式有很大不同。

#### 生产环境配置
1. 下载代码: git clone https://github.com/apache/incubator-dubbo-ops.git

2. 在 dubbo-admin-server/src/main/resources/application-production.properties中指定注册中心地址

3. 构建
    * `mvn clean package`
    启动
    * `mvn --projects dubbo-admin-server spring-boot:run`
    或者
    * `cd dubbo-admin-distribution/target; java -jar dubbo-admin-0.1.jar`
4. 访问 http://localhost:8080

### Swagger 支持

部署完成后，可以访问 http://localhost:8080/swagger-ui.html 来查看所有的restful api

### 参考：

https://github.com/apache/incubator-dubbo-ops/blob/develop/README_ZH.md

### 如何启动com.alibaba.dubbo.container.Main
dubbo所依赖的包
````
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring</artifactId>
    <version>2.5.6.SEC03</version>
</dependency>
<dependency>
    <groupId>org.javassist</groupId>
    <artifactId>javassist</artifactId>
    <version>3.15.0-GA</version>
</dependency>
<dependency>
    <groupId>org.jboss.netty</groupId>
    <artifactId>netty</artifactId>
    <version>3.2.5.Final</version>
</dependency>
````

dubbo的容器
````
JettyContainer
Log4JContainer
LogbackContainer
SpringContainer
````
默认加载是spring容器
````
public void start() {
    String configPath = ConfigUtils.getProperty("dubbo.spring.config");
    if (configPath == null || configPath.length() == 0) {
        configPath = "classpath*:META-INF/spring/*.xml";
    }

    context = new ClassPathXmlApplicationContext(configPath.split("[,\\s]+"));
    context.start();
}
````
如何让进程不终止
````
Class var9 = Main.class;
    synchronized(Main.class) {
        while(running) {
            try {
                Main.class.wait();
            } catch (Throwable var5) {
                ;
            }
        }

    }
````

### 日志是怎么集成的

com.alibaba.dubbo.common.logger.LoggerAdapter
JclLoggerAdapter
JdkLoggerAdapter
Log4jLoggerAdapter
Slf4jLoggerAdapter

Log4jLoggerAdapter为默认适配器

com.alibaba.dubbo.common.logger.LoggerFactory

### 监控中心

### 支持多种协议
dubbo,RMI,hessian,http,webservice,Thrift
配置支持hessian，引入jar
````
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>3.0-alpha-1</version>
</dependency>
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
    <version>4.0.60</version>
</dependency>
<dependency>
    <groupId>org.mortbay.jetty</groupId>
    <artifactId>jetty</artifactId>
    <version>7.0.0.pre5</version>
</dependency>
````
META-INF/spring/order-provider.xml
````
<!--支持hessian协议-->
<dubbo:protocol name="hessian" port="8090" server="jetty"/>
<!--服务暴露的配置，需要暴露的服务接口-->
<bean id="orderService" class="com.tom.service.OrderServiceImpl"/>
<dubbo:service interface="com.tom.IOrderService" ref="orderService" protocol="hessian,dubbo"/>
````
成功后可以看到
url hessian://192.168.238.2:8090/com.tom.IOrderService
url dubbo://192.168.238.2:20880/com.tom.IOrderService

消费者如何调用
gs-dubbo-user/pom.xml增加hessian的依赖包
````
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
</dependency>
````
META-INF/spring/order-provider.xml指定hessian协议
````
<dubbo:reference interface="com.tom.IOrderService" id="orderServices" protocol="dubbo" check="false"/>
<dubbo:reference interface="com.tom.IOrderQueryService" id="orderQueryServices" protocol="hessian" check="false"/>
````
如果在dubbo中需要用到@service注解
META-INF/spring/order-provider.xml
````
<?xml version="1.0" encoding="utf-8" ?>
<beans xmlns="http://www.springframework.org/schema/beans"    xmlns:context="http://www.springframework.org/schema/context"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
            http://code.alibabatech.com/schema/dubbo
            http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <context:annotation-config/>
    <context:component-scan base-package="com.tom.service"/>
<beans/>
````
pom.xml引入jar
````
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>5.0.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.5.RELEASE</version>
</dependency>
````
dubbo在使用时要排除spring2.5.6版本，否则不能用注解
````
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <exclusions>
            <exclusion>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
            </exclusion>
             <exclusion>
                 <groupId>commons-logging</groupId>
                 <artifactId>commons-logging</artifactId>
             </exclusion>
             <exclusion>
                 <groupId>org.springframework</groupId>
                 <artifactId>spring</artifactId>
             </exclusion>
             <exclusion>
                 <groupId>com.alibaba</groupId>
                 <artifactId>fastjson</artifactId>
             </exclusion>
         </exclusions>
</dependency>
````
### 多注册中心
````
<!--dubbo这个服务所要暴露的服务地址所对应的注册中心-->
<!--<dubbo:registry address="N/A"/>-->
<dubbo:registry id="regOne" address="zookeeper://192.168.238.105?backup=192.168.238.110,192.168.238.115,192.168.238.120"/>
<dubbo:registry id="regTwo" address="zookeeper://192.168.238.105?backup=192.168.238.110,192.168.238.115,192.168.238.120"/>

    <dubbo:service interface="com.tom.IOrderService" ref="orderService" protocol="hessian,dubbo" registry="regOne"/>

    <dubbo:service interface="com.tom.IOrderQueryService" ref="orderQueryService" protocol="hessian"  registry="regTwo"/>
````
### 多版本支持
META-INF\spring\order-provider.xml
````
<!--dubbo是长联连-->
<dubbo:service interface="com.tom.IOrderService" ref="orderService" protocol="hessian,dubbo" version="1.0"/>
<dubbo:service interface="com.tom.IOrderService" ref="orderServiceV2" protocol="hessian,dubbo" version="2.0"/>
<dubbo:service interface="com.tom.IOrderQueryService" ref="orderQueryService" protocol="hessian" version="1.0"/>
````
消费端
````
<dubbo:reference interface="com.tom.IOrderService" id="orderServices" protocol="dubbo" check="false" version="1.0"/>
<dubbo:reference interface="com.tom.IOrderService" id="orderServicesV2" protocol="dubbo" check="false" version="2.0"/>
<dubbo:reference interface="com.tom.IOrderQueryService" id="orderQueryServices" protocol="hessian" check="false" version="1.0"/>
````
### 异步调用
order-consumer.xml
````
<!--异步调用,经测试只支持dubbo，不支持hessian-->
<dubbo:reference interface="com.tom.IOrderQueryService" id="orderQueryServiceAsync" protocol="dubbo"
          version="1.0">
<dubbo:method name="doQueryOrder" async="true"/>
</dubbo:reference>
````
这样调用
````
//异步调用
IOrderQueryService orderQueryServiceAsync = (IOrderQueryService) context.getBean("orderQueryServiceAsync");
orderQueryServiceAsync.doQueryOrder("Green异步调用");
Future<String> future = RpcContext.getContext().getFuture();
System.out.println(future.get());
````
### 主机绑定
查看源码
org.apache.dubbo.config.ServiceConfig#findConfigedHosts
META-INF/spring/order-provider.xml
````
<!--当前服务所依赖的协议：webservice,thrift,hessian,http-->
<dubbo:protocol name="dubbo" port="20880" host="192.168.1.104" />
````
### 服务只订阅
META-INF/spring/order-provider.xml
````
<dubbo:registry address="zookeeper://192.168.238.105?backup=192.168.238.110,192.168.238.115,192.168.238.120" register="false"/>
````
### 服务只注册
META-INF/spring/order-provider.xml
````
<dubbo:registry address="zookeeper://192.168.238.105?backup=192.168.238.110,192.168.238.115,192.168.238.120" subscribe="false"/>
````
### 负载均衡

* org.apache.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance
    consistenthash 一致性hash,对于同样的参数会落到同一台机
* org.apache.dubbo.rpc.cluster.loadbalance.LeastActiveLoadBalance
    leastactive 最少活跃调用数，对于响应时间比较短的服务会优先调
* org.apache.dubbo.rpc.cluster.loadbalance.RandomLoadBalance
    random 默认为随机
* org.apache.dubbo.rpc.cluster.loadbalance.RoundRobinLoadBalance
    roundrobin 轮询，按照公约后的权重设置轮询比率
    
   配置 META-INF/spring/order-provider.xml
    ````
    <dubbo:service interface="com.tom.IOrderService" ref="orderService" protocol="hessian,dubbo" version="1.0" loadbalance="roundrobin"/>
    ````
 
### 服务的超时时间

配置 META-INF/spring/order-provider.xml 一般设置时间为5秒
````
<dubbo:service interface="com.tom.IOrderService" ref="orderService" protocol="hessian,dubbo" version="1.0" timeout="5000"/>
````
在实现中休息5秒
````
@Override
public DoOrderResponse doOrder(DoOrderRequest request) {
    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("你曾经来过："+request);
    DoOrderResponse response = new DoOrderResponse();
    response.setCode("0");
    response.setMemo("处理成功");
    return response;
}
````
发错误时时可以看到如下信息
````
Caused by: com.alibaba.dubbo.remoting.TimeoutException: Waiting server-side response timeout. start time: 2019-02-16 11:18:23.004
````