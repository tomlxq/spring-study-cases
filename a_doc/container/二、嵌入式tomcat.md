# 二、嵌入式tomcat

## web技术栈

### servlet传统技术栈

`org.springframework.web.context.ContextLoaderListener`

`org.springframework.context.ApplicationContext`

### web flux(netty)

## Web自动装配

### API角度

Servlet3.0+API实现了Web自动装配`ServletContainerInitializer`

### 容器角度

传统的app部署，是将APP应用部署到servlet容器中

嵌入式app部署，灵活部署，任意指定位置或复杂的条件

tomcat7是servlet 3.0的实现，`ServletContainerInitializer`

tomcat8是servlet 3.1的实现，NIO `HttpServletRequest`、`HttpServletResponse`

> NIO并非一定能够提高性能，比如请求数据量较大，NIO性能比BIO还要差。
>
> NIO多工，读，写，同步的

#### jar启动

`publicclass JarFile extends ZipFile` jar和zip文件没啥区别

`mvn -Dmaven.test.skip -U clean package`

`java -jar` 或`jar` 读取`META-INF\MANIFEST.MF`，为引导类`Main-Class`

```
Manifest-Version: 1.0
Main-Class: org.apache.tomcat.maven.runner.Tomcat7RunnerCli
```

> 参考jdk API: `java.util.jar.Manifest`

`java -jar target/tomcat-war-demo-1.0-snapshot-war-exec.jar`

修改端口，可以查看源码`Tomcat7RunnerCli.java`

`java -jar target/tomcat-war-demo-1.0-snapshot-war-exec.jar  -httpPort=7070`

#### Maven插件

* `tomcat7-maven-plugin`

  查看源码，首选引入到工程

  ```xml
  <dependency>
      <groupId>org.apache.tomcat.maven</groupId>
      <artifactId>tomcat7-maven-plugin</artifactId>
      <version>2.2</version>
  </dependency>
  ```

  调用链路： `org.apache.tomcat.maven.runner.Tomcat7RunnerCli#main`->`org.apache.tomcat.maven.runner.Tomcat7Runner#run`

* `tomcat8-maven-plugin`

## API编程

### `Embedded/tomcat`创建tomcat实例 

`org.apache.catalina.startup.Tomcat`

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
</dependency>
```

### `Service`

```xml
<Service name="Catalina">
</Service >
```

### `Engine`

```xml
<Engine name="Catalina" defaultHost="localhost">
</Engine>
```

### 设置Host对象

```java
Host host = tomcat.getHost();
host.setName("localhost");
host.setAppBase("webapps");
```

### `Connector`

```java
 /**
  * <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" URIEncoding=
  * "UTF-8"/>
  */
Connector connector = tomcat.getConnector();
connector.setURIEncoding("UTF-8");
```

### `Context`

```java
/**
 * 设置context <Context docBase="${app_path}\src\main\webapp" path="/" reloadable="true"/>
 */
String contextPath = "/";
String webapp = webRoot + "src" + File.separator + "main" + File.separator + "webapp";
Context context = tomcat.addWebapp(contextPath, webapp);
if (context instanceof StandardContext) {
    StandardContext ctx = (StandardContext)context;
    /**
     * <Context> <WatchedResource>WEB-INF/web.xml</WatchedResource>
     * <WatchedResource>${catalina.base}/conf/web.xml</WatchedResource> </Context> 设置默认的web.xml到Context
     * webXml=${app_path}\target\classes\conf\web.xml
     */
    String webXml = classPath + File.separator + "conf" + File.separator + "web.xml";
    ctx.setDefaultWebXml(webXml);
    ctx.setReloadable(true);
    // 增加servlet到tomcat容器
    Wrapper demoServlet = tomcat.addServlet(contextPath, "DemoServlet", new DemoServlet());
    demoServlet.addMapping("/demo");
}
```

### 配置端口

```
tomcat.setPort(9090);
```

## Spring Boot嵌入式tomcat编程

### 实现`EmbeddedServletContainerCustomizer`

```
@Configuration
public class TomcatConfiguration implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer cec) {
        if (cec instanceof TomcatEmbeddedServletContainerFactory) {
            TomcatEmbeddedServletContainerFactory factory =
                (TomcatEmbeddedServletContainerFactory)cec;
            Connector connector = new Connector();
            connector.setURIEncoding("UTF-8");
            connector.setPort(9090);
            connector.setProtocol("HTTP/1.1");
            factory.addAdditionalTomcatConnectors(connector);
        }
    }
}
```

### `ConfigurableEmbeddedServletContainer` 	

### `EmbeddedServletContainer`

### `TomcatContextCustomizer`

```java
@Override
public void customize(ConfigurableEmbeddedServletContainer cec) {
    String webRoot = System.getProperty("user.dir") + File.separator + "tomcat-war-demo" + File.separator;
    String classPath = webRoot + "target" + File.separator + "classes";
    String contextPath = "/";
    String webapp = webRoot + "src" + File.separator + "main" + File.separator + "webapp";
    if (cec instanceof TomcatEmbeddedServletContainerFactory) {
        TomcatEmbeddedServletContainerFactory factory = (TomcatEmbeddedServletContainerFactory)cec;
        // 相当于new ContextCustomizer
        factory.addContextCustomizers((context) -> {
            if (context instanceof StandardContext) {
                StandardContext ctx = (StandardContext)context;
                String webXml = classPath + File.separator + "conf" + File.separator + "web.xml";
                ctx.setDefaultWebXml(webXml);
                ctx.setReloadable(true);
                context.setDocBase(webapp);
                context.setPath(contextPath);
            }
        });
    }
}
```

### `TomcatConnectorCustomizer`

```java
// 相当于new TomcatConnectorCustomizer()
factory.addConnectorCustomizers(connector -> {
    connector.setPort(9090);
    connector.setURIEncoding(StandardCharsets.UTF_8.name());
    connector.setProtocol("HTTP/1.1");
});
```

