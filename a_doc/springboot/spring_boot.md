# spring boot项目



## 构建多模块应用

1. 修改主工程的类型

   ``` xml
   <packaging>pom</packaging>
   ```

2. 新建web Maven模块，将所有src移到web/src
3. 再从web工程中独立出model工程
4. 将web工程依赖model工程
5. 重复步骤3，独立出persistence



## 打包

```cmd
mvn -Dmaven.test.skip -U clean package
```

执行可运行的jar或war

1. java -jar {xxxx}.jar
   {xxxx}.jar中没有主清单属性

{jdk}/jre/lib/rt.jar!/META-INF/MANIFEST.MF

java.util.jar.Manifest

需要在web工程加上spring boot的插件

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

因为我使用**spring-boot-dependencies**这个BOM代替了**spring-boot-starter-parent**这个parent POM（详见[13.2.2. Using Spring Boot without the parent POM](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using-boot-maven-without-a-parent)）

导致spring-boot-maven-plugin的配置项丢失，使得打包后的jar中的MANIFEST.MF文件缺少Main-Class。

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 运行spring-boot工程

```cmd
{dir}\gs-spring-boot-webflux\web>mvn spring-boot:run
{dir}\gs-spring-boot-webflux\web\target>java -jar web-0.0.1-SNAPSHOT.jar
```

查看启动参数

`org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration`

如端口

--server.port=8080



![1560586019071](img\1560586019071.png)

## 参考资料

* https://reactivex.io

* https://github.com/mercyblitz/jsr
* https://projectreactor.io/docs





