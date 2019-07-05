# 一、Spring Boot初体验

## Spring Boot技术栈

介绍Spring Boot完整的技术栈，比如Web应用，数据操作，消息，测试及运维管理

## Spring Boot构建方式

图形化，命令行

## Spring Boot多模块应用

构建分层，多模块SpringBoot应用

## Spring Boot运行方式

IDEA启动

命令行启动

Maven插件启动

## Spring Boot简单应用

Spring Web MVC 

Spring web flux

## Spring Boot三大特性

自动装配、嵌入式容器、为生产准备的特性

```
org.springframework.web.servlet.HandlerInterceptor
preHandle
postHandle
afterCompletion

javax.servlet.Filter#doFilter
AOP相关
org.springframework.aop.BeforeAdvice
org.springframework.aop.AfterAdvice
org.springframework.aop.AfterReturningAdvice
```



```java
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory
		implements ConfigurableTomcatWebServerFactory, ResourceLoaderAware
		
public class JettyServletWebServerFactory extends AbstractServletWebServerFactory
		implements ConfigurableJettyWebServerFactory, ResourceLoaderAware 
		
		
		org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
```

