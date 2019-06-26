## 介绍Environment仓储

- {application}配置使用客户端名称
- {profile}客户端spring.profiles.active配置
- {label}服务端配置文件版本标识

## Spring Cloud分布式配置

- GIT实现
  * 服务端配置
    * spring.cloud.config.server.git.uri
    * spring.cloud.config.server.git.*
  * 客户端配置
    * spring.cloud.config.uri
    * spring.cloud.config.name
    * spring.cloud.config.profile
    * spring.cloud.config.label

## 动态配置属性Bean

## 健康指标

构建Spring Cloud配置服务器

1. 在configuration class标记`@EnableConfigServer`
2. 配置文件目录
   * app.properties默认
   * app-dev.properties(profile=dev开发环境)
   * app-test.properties(profile=test测式环境)
   * app-staging.properties(profile=staging预发布环境)
   * app-prod.properties(profile=prod生产环境)

3. application.properties

   * 本地读取classpath目录

    ````xml
   spring.cloud.config.server.native.search-locations=classpath:/config
    ````

   * 本地读取绝对目录

   ```xml
   spring.cloud.config.server.git.uri=\
   file:///E:/data/wwwtest/spring-study-cases/gs-spring-cloud-server/src/main/resources/config
   ```

   * 远程GIT

   ```properties
   spring.application.name=config-server
   server.port=8888
   #从读取远程github配置文件####################################################
   spring.cloud.config.server.git.uri=https://github.com/tomlxq/cloud-config-samples.git
   spring.cloud.config.server.git.search-paths=config-client
   #spring.cloud.config.server.git.username=xxxx
   #spring.cloud.config.server.git.password=xxxx
   spring.cloud.config.label=master
   ### 全局关闭Actuator安全
   spring.management.security.enabled=false
   #management.endpoints.web.base-path=/
   management.endpoints.web.exposure.include=*
   management.endpoint.health.show-details=always
   ```

