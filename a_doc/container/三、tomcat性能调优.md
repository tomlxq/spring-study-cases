# 第三、性能调优

## tomcat配置调优

通过tomcat内部配置的方式，如：线程池管理，IO连接器配置，动静分离处理等

### 减少配置优化

#### 场景一：假设当前Rest应用（微服务）

分析：它不需要静态资源，Tomcat容器支持静态和动态

* 静态处理：`DefaultServlet`

  优化方案：通过移除`conf/web.xml`中的`org.apache.catalina.servlets.DefaultServlet`

* 动态：应用`servlet`、`JspServlet`

  优化方案：通过移除`conf/web.xml`中的`org.apache.jasper.servlet.JspServlet`

> Spring web mvc应用servlet：`DispatcherServlet`

* 移除welcome-file-list

  ```xml
  <welcome-file-list>
      <welcome-file>index.html</welcome-file>
      <welcome-file>index.htm</welcome-file>
      <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
  ```

* 移除session设置

  对于大多数Rest API是没有状态的，不需要session

  ```xml
  <session-config>
     <session-timeout>30</session-timeout>
  </session-config>
  ```

  Spring Security OAuth2.0或JWT

  Session通过 jsesssionId用来跟踪用户，Http无状态，需要一个ID与当前用户相关联。Spring session HttpSession，jsessionId存在于Redis实现多个机器登陆，用户session不会消失。

  存储方法：Cookies，URL重写，SSL

* 移除 Valve

  Valve类似于Filter

  `org.apache.catalina.Valve`

  `javax.servlet.Filter#doFilter`

  移除`AccessLogValve`，可以通过Nginx的Access Log替代，Valve实现都需要消耗Java应用的计算时间。

  ```xml
  <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                 prefix="localhost_access_log" suffix=".txt"
                 pattern="%h %l %u %t &quot;%r&quot; %s %b" />
  ```

#### 场景二：需要JSP

分析：`JspServlet`无法移除，了解`JspServlet`处理原理

  > Servlet周期：
  >
  > 实例化：Servlet和Filter实现类必包含默认构造器。反射的方式进行实例化。
  >
  > 初始化：Servlet容器调用Servlet或Filter init()方法。
  >
  > 销毁：Servlet容器关闭时，Servlet或Filter destroy()方法被调用。

  Servlet或Filter在一个容器中，是一般情况在一个Web App中是一个单例，不排除应用定义多个。

  JspServlet相关的优化`ServletConfig`参数:

需要编译

  * compiler
  * modificationTestInterval

不需要编译

* development设置为false

那么这些JSP要如何编译，优化方法：

* Ant Task执行JSP编译

* Maven插件：

  ```xml
   <plugin>
       <groupId>org.apache.sling</groupId>
       <artifactId>jspc-maven-plugin</artifactId>
       <version>2.1.0</version>
       <executions>
           <execution>
               <goals>
                   <goal>jspc</goal>
               </goals>
           </execution>
       </executions>
  </plugin>
  ```

  Jps->翻译成.jsp或.jspx文件成.java->编译.class

总结，conf/web.xml作为Servlet应用默认的web.xml，实际上，应用程序存在两份web.xml,其中包括Tomcat下conf/web.xml和应用的web.xml.最终将两者合并。

JspServlet如果development的参数为true,它会自定检查文件是否修改，如果修改重新翻译，再编译（加载和执行）。言外之意，JspServlet开发模式可能会导致内存溢出。卸载Classic不及时会导致Perm区域不够。



## 程序调优

通过理解servlet和JSP生命周期，合理地编码以及配置实现应用性调优的目的

### 关闭自动重载

`${TOMCAT_HOME}\conf\Catalina\localhost\${CONTEXT_NAME}.xml`

`${TOMCAT_HOME}\conf\Catalina\localhost\TOOT.xml`

`${TOMCAT_HOME}\conf\context.xml`

```xml
<Context docBase="${app_path}" reloadable="false"/>
```

设置`reloadable="false"`

### 调整线程池数量

`${TOMCAT_HOME}\conf\server.xml`

```xml
<!-- 
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" URIEncoding="UTF-8"/>
-->
<Executor name="tomcatThreadPool" namePrefix="catalina-exec-"
          maxThreads="150" minSpareThreads="4"/>	
<Connector executor="tomcatThreadPool"
           port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />	
```

通过程序来理解，`<Executor>`实际的Tomcat接口`org.apache.catalina.Executor`

* 扩张了J.U.C的标准接口`java.util.concurrent.Executor`。

* 实现`org.apache.catalina.core.StandardThreadExecutor`

  线程数量

  ```java
      /**
      * max number of threads
      */
      protected int maxThreads = 200;
  
      /**
      * min number of threads
      */
      protected int minSpareThreads = 25;
      public void setMaxThreads(int maxThreads) {
          this.maxThreads = maxThreads;
          if (executor != null) {
              executor.setMaximumPoolSize(maxThreads);
          }
      }
  
      public void setMinSpareThreads(int minSpareThreads) {
          this.minSpareThreads = minSpareThreads;
          if (executor != null) {
              executor.setCorePoolSize(minSpareThreads);
          }
      }
  ```

  线程池`org.apache.tomcat.util.threads.ThreadPoolExecutor`扩展了java标准的`java.util.concurrent.ThreadPoolExecutor`

  Tomcat IO连接器使用的池程池实际上是标准的Java线程池的扩展，最大线程数量和最小线程数量分别别是`MaximumPoolSize`和`CorePoolSize`



观察`StandardThreadExecutor`，调整线程池数量

评估的一些参考：

1. 正确率
2. Load（CPU->JVM GC)
3. TPS/QPS
4. CPU密集型（加解密，算法），还是IO密集型（网络，文件读写）



线程池名：

`org.apache.tomcat.util.threads.ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, java.util.concurrent.TimeUnit, java.util.concurrent.BlockingQueue<java.lang.Runnable>, java.util.concurrent.RejectedExecutionHandler)`

```java
public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    prestartAllCoreThreads();
}
```

`java.util.concurrent.ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, java.util.concurrent.TimeUnit, java.util.concurrent.BlockingQueue<java.lang.Runnable>, java.util.concurrent.RejectedExecutionHandler)`

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          RejectedExecutionHandler handler) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), handler);
}
```

`java.util.concurrent.Executors#defaultThreadFactory`

```java
public static ThreadFactory defaultThreadFactory() {
    return new DefaultThreadFactory();
}
```

`java.util.concurrent.Executors.DefaultThreadFactory#DefaultThreadFactory`

```java
DefaultThreadFactory() {
    SecurityManager s = System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup() :
    Thread.currentThread().getThreadGroup();
    namePrefix = "pool-" +
        poolNumber.getAndIncrement() +
        "-thread-";
}
```

## JVM参数调优

掌握如何JVM参数的方式调优Tomcat服务器性能，如堆内外内存管理

### 调整堆大小

### 调整GC算法

默认算法

```java
java -jar -server -XX:-PrintGCDetails -Xloggc:./gc.log -XX:+HeapDumpOnOutOfMemoryError -Xms1g -Xmx1g -XX:MaxGCPauseMillis=250 -Djava.awt.headless=true {jar_name}.jar
```

`G1`算法

```java
java -jar -server -XX:-PrintGCDetails -Xloggc:./g1-gc.log -XX:+HeapDumpOnOutOfMemoryError -Xms1g -Xmx1g -XX:+UseG1GC -XX:+UserNUMA -XX:MaxGCPauseMillis=250 -Djava.awt.headless=true {jar_name}.jar
```

`-server`主要是提高吞吐量，在有限的资源，实现最大化利用。`-client`主要是提高响应时间，主要是搞高用户体验



## Spring Boot配置调整

`pom.xml`jar依赖

```xml
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-jasper</artifactId>
</dependency>
```

`application.properties`

```properties
# <Executor name="tomcatThreadPool" namePrefix="catalina-exec-" maxThreads="100" minSpareThreads="10"/>
# <Connector executor="tomcatThreadPool" port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />
# 设置线程池
server.tomcat.max-threads=99
server.tomcat.min-spare-threads=10
# 取消JSP servlet
server.jsp-servlet.registered=false
# 取消tomcat AccessLogValve
server.tomcat.accesslog.enabled=false

# 取消静态资源处理
spring.resources.chain.enabled=false
spring.resources.chain.strategy.content.enabled=false
spring.resources.chain.strategy.fixed.enabled=false
```





严重: Error starting static Resources
java.lang.IllegalArgumentException: Invalid or unreadable WAR file : E:\data\wwwtest\spring-study-cases\tomcat-war-demo\.extract\webapps\ROOT.war

This is indeed a bug in the tomcat7 maven plugin **version 2.2**:https://issues.apache.org/jira/browse/MTOMCAT-263