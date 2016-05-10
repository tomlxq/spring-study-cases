http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
测试国际化
https://justinrodenbostel.com/2014/05/13/part-4-internationalization-in-spring-boot/

想使用JSP需要配置application.properties。 
添加src/main/resources/application.properties内容：

# 页面默认前缀目录
spring.mvc.view.prefix=/WEB-INF/jsp/
# 响应页面默认后缀
spring.mvc.view.suffix=.jsp

在 src/main 下面创建 webapp/WEB-INF/jsp 目录用来存放我们的jsp页面。 
index.jsp

<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Spring Boot Sample</title>
</head>

<body>
    Time: ${time}
    <br>
    Message: ${message}
</body>
</html>

page1.jsp

<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Spring Boot Sample</title>
</head>

<body>
    <h1>${content }</h1>
</body>
</html>

要想让spring-boot支持JSP，需要将项目打成war包。 
我们做最后一点修改，修改pom.xml文件，将 jar 中的 jar 修改为 war

然后启动spring-boot服务。 
<!--        <dependency> -->
<!--            <groupId>org.springframework.boot</groupId> -->
<!--            <artifactId>spring-boot-starter-web</artifactId> -->
<!--        </dependency> -->
<!--        <dependency> -->
<!--            <groupId>org.springframework.boot</groupId> -->
<!--            <artifactId>spring-boot-starter-tomcat</artifactId> -->
<!--        </dependency> -->
<!--        <dependency> -->
<!--            <groupId>org.apache.tomcat.embed</groupId> -->
<!--            <artifactId>tomcat-embed-jasper</artifactId> -->
<!--            <scope>provided</scope> -->
<!--        </dependency> -->
<!--        <dependency> -->
<!--            <groupId>javax.servlet</groupId> -->
<!--            <artifactId>jstl</artifactId> -->
<!--        </dependency> -->