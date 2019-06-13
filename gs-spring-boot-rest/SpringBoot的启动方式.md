



## 使用内置tomcat启动
1. 启动方式：
1、IDEA中main函数启动
2、mvn springboot-run 命令
3、java -jar XXX.jar

2. 配置内置tomcat属性：
关于Tomcat的属性都在org.springframework.boot.autoconfigure.web.ServerProperties配置类中做了定义，我们只需在application.properties配置属性做配置即可。通用的Servlet容器配置都已”server”左右前缀，而Tomcat特有配置都以”server.tomcat”作为前缀。下面举一些常用的例子。

配置Servlet容器：
```properties
#配置程序端口，默认为8080
server.port= 8080
#用户绘画session过期时间，以秒为单位
server.session.timeout=
# 配置默认访问路径，默认为/
server.context-path=
```
配置Tomcat：
```properties
# 配置Tomcat编码,默认为UTF-8
server.tomcat.uri-encoding=UTF-8
# 配置最大线程数
server.tomcat.max-threads=1000
```

## 外置tomcat部署
配置步骤：
1、继承SpringBootServletInitializer
外部容器部署的话，就不能依赖于Application的main函数了，而是要以类似于web.xml文件配置的方式来启动Spring应用上下文，此时我们需要在启动类中继承SpringBootServletInitializer并实现configure方法：

```java
@SpringBootApplication
public class Chapter05Application extends SpringBootServletInitializer {
 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Chapter05Application.class);
    }
 
    public static void main(String[] args) {
        SpringApplication.run(Chapter05Application.class, args);
    }
}
```
这个类的作用与在web.xml中配置负责初始化Spring应用上下文的监听器作用类似，只不过在这里不需要编写额外的XML文件了。
2、pom.xml修改tomcat相关的配置
方法一：

```
<dependency>
    <groupid>org.springframework.boot</groupid>
    spring-boot-starter-web</artifactid>
    <exclusions>
        <exclusion>
            <groupid>org.springframework.boot</groupid>
            spring-boot-starter-tomcat</artifactid>
        </exclusion>
    </exclusions>
</dependency>
```
在这里需要移除对嵌入式Tomcat的依赖，这样打出的war包中，在lib目录下才不会包含Tomcat相关的jar包，否则将会出现启动错误。

还有一个很关键的关键点，就是tomcat-embed-jasper中scope必须是provided。
```
<dependency>
    <groupid>org.apache.tomcat.embed</groupid>
    <artifactId>tomcat-embed-jasper</artifactid>
    <scope>provided</scope>
</dependency>
```
因为SpringBootServletInitializer需要依赖 javax.servlet，而tomcat-embed-jasper下面的tomcat-embed-core中就有这个javax.servlet，如果没用provided，最终打好的war里面会有servlet-api这个jar，这样就会跟tomcat本身的冲突了。这个关键点同样适应于下面说的第二种方法。

方法二：
直接添加如下配置即可：
```
<dependency>
    <groupid>org.springframework.boot</groupid>
    <artifactId>spring-boot-starter-tomcat</artifactid>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupid>org.apache.tomcat.embed</groupid>
    <artifactId>tomcat-embed-jasper</artifactid>
    <scope>provided</scope>
</dependency>
```
provided的作用上面已经介绍的很透彻了，这里就不啰嗦了，这种方式的好处是，打包的war包同时适合java -jar命令启动以及部署到外部容器中。

3、由jar变成war
war
4、注意的问题
此时打成的包的名称应该和application.properties的
server.context-path=/test

保持一致
```
<build>
    <finalname>test</finalname>
</build>
```
如果不一样发布到tomcat的webapps下上下文会变化