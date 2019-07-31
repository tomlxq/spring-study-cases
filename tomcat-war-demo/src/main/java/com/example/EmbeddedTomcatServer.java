package com.example;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/30
 */
public class EmbeddedTomcatServer {
    private static void addDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        // Otherwise the default location of a Spring DispatcherServlet cannot be set
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMapping("/", "default");
        // context.addServletMappingDecoded("/", "default");
    }

    public static void main(String[] args) throws LifecycleException, InterruptedException, ServletException {
        // webRoot=${app_path}\target
        String webRoot = System.getProperty("user.dir") + File.separator + "tomcat-war-demo" + File.separator;
        // classPath=${app_path}\target\classes
        String classPath = webRoot + "target" + File.separator + "classes";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9090);
        /**
         * <Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" URIEncoding=
         * "UTF-8"/>
         */
        Connector connector = tomcat.getConnector();
        connector.setURIEncoding("UTF-8");
        /**
         * 设置Host <Host name="localhost" appBase="webapps" unpackWARs="true" autoDeploy="true">
         */
        Host host = tomcat.getHost();
        host.setName("localhost");
        host.setAppBase("webapps");
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

            // addDefaultServlet(context);
            // 增加servlet到tomcat容器
            Wrapper demoServlet = tomcat.addServlet(contextPath, "DemoServlet", new DemoServlet());
            demoServlet.addMapping("/demo");

        }
        // 启动Tomcat服务器
        tomcat.start();
        // 强制tomcat server 等待，避免main线程执行结束关闭
        tomcat.getServer().await();
        // http://localhost:9090/index.jsp
    }
}
