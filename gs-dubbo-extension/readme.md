# 源码阅读 dubbo扩展

### org.apache.dubbo.common.extension.ExtensionLoader
源码入口
### org.apache.dubbo.config.spring.ServiceBean
读取dubbo配置
### META-INF/dubbo.xsd
###  META-INF/spring.handlers

````
http\://dubbo.apache.org/schema/dubbo=org.apache.dubbo.config.spring.schema.DubboNamespaceHandler
http\://code.alibabatech.com/schema/dubbo=org.apache.dubbo.config.spring.schema.DubboNamespaceHandler
 ````
### org.apache.dubbo.config.spring.schema.DubboNamespaceHandler

读出来，并放到相应的配置里面去

### org.apache.dubbo.config.ServiceConfig


org.apache.dubbo.config.spring.ServiceBean#afterPropertiesSet
初始化，并export()，发布provider
