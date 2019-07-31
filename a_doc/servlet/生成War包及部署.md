# 生成War包及部署

## IDEA创建Servlet项目详细步骤

1、Create New Project -> Java EE -> Web Application -> 项目命名 -> Finish

2、web/WEB-INF中添加classes、lib、src文件夹

File -> Project Structture -> Modules

在web目录下新建WEB-INF目录，并在WEB-INF目录下新建3个目录：classes、lib、src，其中src右键设为source

src用于存放源Servlet的java文件，classes用来存放编译后输出的class文件，lib用于存放第三方jar包

3、配置classes与lib文件夹路径

File -> Project Structure  -> Module -> Paths -> Use module compile output path" -> 将Output path和Test output path都选择刚刚创建的classes文件夹

4、配置tomcat容器

Run -> Edit Configuration -> 点击左上角“+”号 -> “Tomcat Server” -> “Local”

## Servlet 3.1.0后的配置

### Servlet采用注解

```java
@WebServlet(urlPatterns = "/demo")
public class DemoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = req.getParameter("message");
        resp.getWriter().println(message);
    }
}
```

### pom.xml中配置

不需要`webapp/WEB-INF/web.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-study-cases</artifactId>
        <groupId>com.tom</groupId>
        <version>2.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>tomcat-war-demo</artifactId>
    <packaging>war</packaging>
    <dependencies>
        <!-- @WebServlet需要这个依赖必需Version大于3.1.0 -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <!-- provide 不包括在打包的war的lib库里 -->
            <scope>provided</scope> 
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.2</version>
                <configuration>
                    <!-- 找不到web.xml不报错 -->
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

### 打war包

`mvn -Dmaven.test.skip -U clean package`

## 部署war

### webapps下部署

${TOMCAT_HOME}\webapps\tomcat-war-demo-2.0.0.war

并访问页

`http://localhost:8080/tomcat-war-demo-2.0.0/demo?message=tom`

### 修改`server.xml`的host外置应用部署

在Engine/host/下增加节点，如：

```xml
<Context docBase="${app_absolute_path}" path="/" reloadable="true"/>
```

* docBase 可以是绝对路径或相对路径
* path web应用的上下文件路径，用于外部访问

配置好了之后，重启应用，并访问页

`http://localhost:8080/demo?message=abc`

> 配置仅覆盖了ROOT，其它任然可以访问如
>
> `http://localhost:8080/manager`（用户名密码在conf/tomcat-users.xml里配置的）
>
> `http://localhost:8080/examples`
>
> `http://localhost:8080/host-manager`
>
> `http://localhost:8080/docs/`

### 配置独立的context配置文件

`${TOMCAT_HOME}\conf\${ENGINE_NAME}\${HOST_NAME}\${ContextPath}.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context docBase="${app_absolute_path}" reloadable="true"/>
```

> ${ENGINE_NAME}为Engine name，默认为`Catalina`
>
> `<Engine name="Catalina" defaultHost="localhost">`
>
> ${HOST_NAME}为Host name，默认为`localhost`
>
> `<Host name="localhost"  appBase="webapps" unpackWARs="true" autoDeploy="true">`
>
> 此时独立Context配置path="${value}" 没有用

如 ${ContextPath}=abc，则这样访问

`http://localhost:8080/abc/demo?message=abc`

> 注：不支持动态部署，建议在生产环境使用

### 配置独立的context ROOT配置文件

`${TOMCAT_HOME}\conf\${ENGINE_NAME}\${HOST_NAME}\ROOT.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context docBase="${app_absolute_path}" reloadable="true"/>
```

则这样访问

`http://localhost:8080/demo?message=abc`

> 注：支持动态部署，建议在开发环境使用

`org.apache.catalina.Context`

```java
/**
* Set the document root for this Context. This can be either an absolute
* pathname or a relative pathname. Relative pathnames are relative to the
* containing Host's appBase.
*
* @param docBase The new document root
*/
public void setDocBase(String docBase);

/**
     * Set the context path for this web application.
     *
     * @param path The new context path
     */
public void setPath(String path);
```

`org.apache.catalina.core.StandardContext`

```java
/**
* The document root for this web application.
*/
private String docBase = null;
```

## References:

1. http://tomcat.apache.org/tomcat-7.0-doc/config/index.html