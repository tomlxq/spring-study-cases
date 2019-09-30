# Spring Boot思想

## 用命令构建工程

`mvn archetype:generate -DgroupId=thinking.springboot.com -DartifactId=thinking-springboot-demo -Dversion=1.0-SNAPSHOT -DinteractiveMode=false -Dpackage=thinking.springboot.com`

## 在github上建空工程，并将本地的工程代码推到github上

`git remote add origin git@github.com:tomlxq/thinking-springboot-demo.git`

`git push -u origin master `

## 增加`spring-boot-starter-parent`和`spring-boot-start-web`

vi `pom.xml`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.8.RELEASE</version>
</parent>
...
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.1.8.RELEASE</version>
</dependency>
```

查看目录树，了解依赖情况

`mvn dependency:tree -Dincludes=org.springframework.*`

```shell
[INFO] \- org.springframework.boot:spring-boot-starter-web:jar:2.1.8.RELEASE:compile
[INFO]    +- org.springframework.boot:spring-boot-starter:jar:2.1.8.RELEASE:compile
[INFO]    |  +- org.springframework.boot:spring-boot:jar:2.1.8.RELEASE:compile
[INFO]    |  +- org.springframework.boot:spring-boot autoconfigure:jar:2.1.8.RELEASE:compile
[INFO]    |  \- org.springframework.boot:spring-boot-starter-logging:jar:2.1.8.RELEASE:compile
[INFO]    +- org.springframework.boot:spring-boot-starter-json:jar:2.1.8.RELEASE:compile
[INFO]    \- org.springframework.boot:spring-boot-starter-tomcat:jar:2.1.8.RELEASE:compile

```

`mvn spring-boot:run -Dserver.port=8004`

`curl http://192.168.238.150:8004`

## 制作FatJar独自运行jar包

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

`mvn package`

`java -jar thinking-springboot-demo-1.0-SNAPSHOT.jar`



## FatJar运行机制

### 检查主类`META-INF/MANIFEST.MF`

`unzip target/thinking-springboot-demo-1.0-SNAPSHOT.jar -d /temp`

```cmd
[root@localhost temp]# cat META-INF/MANIFEST.MF 
Manifest-Version: 1.0
Implementation-Title: thinking-springboot-demo
Implementation-Version: 1.0-SNAPSHOT
Start-Class: thinking.springboot.com.App
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Build-Jdk-Spec: 1.8
Spring-Boot-Version: 2.1.8.RELEASE
Created-By: Maven Archiver 3.4.0
Main-Class: org.springframework.boot.loader.JarLauncher
```

`cd /temp`

### 启动主类 

`java org.springframework.boot.loader.JarLauncher`

`org.springframework.boot.loader.WarLauncher`

`org.springframework.boot.loader.JarLauncher`
## 不以parent引入依赖

```xml
<properties>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>
    <resource.delimiter>@</resource.delimiter>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.target>${java.version}</maven.compiler.target>
</properties>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>2.1.8.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
...
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>3.1.0</version>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>2.1.8.RELEASE</version>
        </plugin>
    </plugins>
</build>
```

## 采用Tomcat运行方式

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

`o.s.b.w.embedded.tomcat.TomcatWebServer`

## 采用Jetty运行方式

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

`o.s.b.web.embedded.jetty.JettyWebServer `

## 采用Undertow运行方式

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

`o.s.b.w.e.u.UndertowServletWebServer `

### spring-boot-starter-web与spring-boot-starter-webflux共享，会忽略webflux

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

### 注掉`spring-boot-starter-web`，增加WebFlux函数式Endpoint

```java
@Bean
public RouterFunction<ServerResponse> helloWorld() {
    return route(GET("/hello"), request -> ok().body(Mono.just("hello, webflux!"), String.class));
}
```

`ReactiveWebServerApplicationContext`

`ServletWebServerApplicationContext`

### 查看WebFlux的webserver实现类

```java
@EventListener(WebServerInitializedEvent.class)
public void onReady(WebServerInitializedEvent event) {
    System.out.println("当前web server的实现类为：" + event.getWebServer().getClass().getName());
}

/**
     * ApplicationRunner 在spring boot启动后会回调
     *
     * @param context
     * @return
     */
@Bean
public ApplicationRunner runner(WebServerApplicationContext context) {
    return args -> {
        System.out.println("当前web server的实现类为：" + context.getWebServer().getClass().getName());
    };
}
```

分别查看tomcat,jetty,undertow都成功的执行了。

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-tomcat</artifactId>
</dependency>
<!--<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>-->
<!--         <dependency>
                     <groupId>org.springframework.boot</groupId>
                     <artifactId>spring-boot-starter-undertow</artifactId>
                 </dependency>-->
```

## 理解自动装配

### @SpringBootApplication的属性别名

调整启动类

```java
@SpringBootApplication(scanBasePackages = {"thinking.springboot.com.config", "thinking.springboot.com.controller"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

增加配置类

```java
@Configuration
public class AppConfig {
    @EventListener(WebServerInitializedEvent.class)
    public void onReady(WebServerInitializedEvent event) {
        System.out.println("当前web server的实现类为：" + event.getWebServer().getClass().getName());
    }

    /**
     * ApplicationRunner 在spring boot启动后会回调
     *
     * @param context
     * @return
     */
    @Bean
    public ApplicationRunner runner(WebServerApplicationContext context) {
        return args -> {
            System.out.println("当前web server的实现类为：" + context.getWebServer().getClass().getName());
        };
    }

    @Bean
    public RouterFunction<ServerResponse> helloWorld() {
        return route(GET("/hello"), request -> ok().body(Mono.just("hello, webflux!"), String.class));
    }
}
```

重新运行后，一切正常

### @SpringBootApplication标注非引导类

调整启动类`App.java`

```java
public class App {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }
}
```

调整配置类`AppConfig.java`

```java
@SpringBootApplication(scanBasePackages = "thinking.springboot.com.controller")
public class AppConfig {
    @EventListener(WebServerInitializedEvent.class)
    public void onReady(WebServerInitializedEvent event) {
        System.out.println("当前web server的实现类为：" + event.getWebServer().getClass().getName());
    }

    /**
     * ApplicationRunner 在spring boot启动后会回调
     *
     * @param context
     * @return
     */
    @Bean
    public ApplicationRunner runner(WebServerApplicationContext context) {
        return args -> {
            System.out.println("当前web server的实现类为：" + context.getWebServer().getClass().getName());
        };
    }

    @Bean
    public RouterFunction<ServerResponse> helloWorld() {
        return route(GET("/hello"), request -> ok().body(Mono.just("hello, webflux!"), String.class));
    }
}
```

重新运行后，一切正常

### @EnableAutoConfiguration激活自动装配

调整配置类`AppConfig.java`,去掉`@SpringBootApplication(scanBasePackages = "thinking.springboot.com.controller")`

```java
@EnableAutoConfiguration
@ComponentScan(basePackages = "thinking.springboot.com.controller")
public class AppConfig {
    @EventListener(WebServerInitializedEvent.class)
    public void onReady(WebServerInitializedEvent event) {
        System.out.println("当前web server的实现类为：" + event.getWebServer().getClass().getName());
    }

    /**
     * ApplicationRunner 在spring boot启动后会回调
     *
     * @param context
     * @return
     */
    @Bean
    public ApplicationRunner runner(WebServerApplicationContext context) {
        return args -> {
            System.out.println("当前web server的实现类为：" + context.getWebServer().getClass().getName());
        };
    }

    @Bean
    public RouterFunction<ServerResponse> helloWorld() {
        return route(GET("/hello"), request -> ok().body(Mono.just("hello, webflux!"), String.class));
    }
}
```

重新运行后，一切正常

### @SpringBootApplication继承@Configuration提升CGLIB特性

调整配置类`AppConfig.java`,增加Bean

```java
@Bean
public ApplicationRunner runner2(BeanFactory beanFactory) {
    return args -> {
        System.out.println("当前helloWorld的实现类为：" + beanFactory.getBean("helloWorld").getClass().getName());
        System.out.println("当前web configuration的实现类为：" + beanFactory.getBean(AppConfig.class).getClass().getName());
    };
}
```

重新运行后

```cmd
当前helloWorld的实现类为：org.springframework.web.reactive.function.server.RouterFunctions$DefaultRouterFunction
当前web configuration的实现类为：thinking.springboot.com.config.AppConfig

```

再次配置类`AppConfig.java`

```java
@SpringBootApplication(scanBasePackages = "thinking.springboot.com.controller")
public class AppConfig {
...
}
```

重新运行后

```cmd
当前helloWorld的实现类为：org.springframework.web.reactive.function.server.RouterFunctions$DefaultRouterFunction
当前web configuration的实现类为：thinking.springboot.com.config.AppConfig$$EnhancerBySpringCGLIB$$3ebb768a
```

### 理解自动配置机制

自动配置，是当标注为`@Configuration`的类，标注`@ConditionalOnClass`，当类在classpath下才装配

@ConditionalOnMissingBean

```java
public enum EmbeddedDatabaseConnection {
    ...
    /**
	 * HSQL Database Connection.
	 */
	HSQL(EmbeddedDatabaseType.HSQL, "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:%s");
    ...
    /**
	 * Returns the most suitable {@link EmbeddedDatabaseConnection} for the given class
	 * loader.
	 * @param classLoader the class loader used to check for classes
	 * @return an {@link EmbeddedDatabaseConnection} or {@link #NONE}.
	 */
	public static EmbeddedDatabaseConnection get(ClassLoader classLoader) {
		for (EmbeddedDatabaseConnection candidate : EmbeddedDatabaseConnection.values()) {
			if (candidate != NONE && ClassUtils.isPresent(candidate.getDriverClassName(), classLoader)) {
				return candidate;
			}
		}
		return NONE;
	}
}
```

`get(ClassLoader classLoader)`的方法用于`DataSourceAutoConfiguration.EmbeddedDatabaseCondition`

```java
@Configuration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ DataSourcePoolMetadataProvidersConfiguration.class, DataSourceInitializationConfiguration.class })
public class DataSourceAutoConfiguration {

	@Configuration
	@Conditional(EmbeddedDatabaseCondition.class)
	@ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
	@Import(EmbeddedDataSourceConfiguration.class)
	protected static class EmbeddedDatabaseConfiguration {

	}
	...
    /**
	 * {@link Condition} to detect when an embedded {@link DataSource} type can be used.
	 * If a pooled {@link DataSource} is available, it will always be preferred to an
	 * {@code EmbeddedDatabase}.
	 */
	static class EmbeddedDatabaseCondition extends SpringBootCondition {

		private final SpringBootCondition pooledCondition = new PooledDataSourceCondition();

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("EmbeddedDataSource");
			if (anyMatches(context, metadata, this.pooledCondition)) {
				return ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
			}
			EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
			if (type == null) {
				return ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll());
			}
			return ConditionOutcome.match(message.found("embedded database").items(type));
		}

	}

}
```

当`DataSourceAutoConfiguration.EmbeddedDatabaseCondition`匹配后，即`org.hsqldb.jdbcDriver`在classpath下，@Configuration类`DataSourceAutoConfiguration.EmbeddedDatabaseCondition`会被装配，故`@Import(EmbeddedDataSourceConfiguration.class)`会被导入

`DataSourceAutoConfiguration`如何被装配

`spring-boot-autoconfigure/${version}/spring-boot-autoconfigure-${version}.jar!/ 

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
...
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
...
```

## 创建自动配置类

调整启动类`App.java`

```java
@EnableAutoConfiguration
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
```

调整配置类`AppConfig.java`,增加Bean

```java
@Configuration
@ComponentScan(basePackages = "thinking.springboot.com.controller")
public class AppConfig {
    ...
}
```

增加`src/main/resources/META-INF/spring.factories`

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  thinking.springboot.com.config.AppConfig
```

重新运行后,可以看到自动装配生效了。

## 理解production-ready特性

### `spring-boot-starter-actuator`

GAV

```xml
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

在application.properties

```properties
management.endpoints.web.exposure.include=*
```

或在启动时，定义要暴露的Endpoints.

`mvn spring-boot:run -Dmanagement.endpoints.web.exposure.include=beans,conditions,env`

### 理解外部化配置

#### 通过Bean的@Value注入

#### Spring的Environment读取

#### `@ConfigurationProperties`绑定到结构化对象



1. 增加配置项`src/main/resources/application.properties`

`spring.application.name=thinking-springboot`

2. ExternalProperties.java配置项类

   ```java
   @ConfigurationProperties(prefix = "thinking.springboot")
   @Setter
   @Getter
   public class ExternalProperties {
       private String host = "localhost";
   }
   ```

3. `ExternalConfig.java`通过三种方式读取配置项

```java
@Configuration
@EnableConfigurationProperties(ExternalProperties.class)
public class ExternalConfig {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private Environment env;
    @Autowired
    private ExternalProperties properties;
    @Bean
    public ApplicationRunner runner3(WebServerApplicationContext context) {
        return args -> {
            System.out.println("Env -> spring.application.name:" + env.getProperty("spring.application.name"));
            System.out.println("Value -> spring.application.name:" + name);
            System.out.println("ConfigurationProperties -> thinking.springboot.host:" + properties.getHost());
        };
    }
}
```



