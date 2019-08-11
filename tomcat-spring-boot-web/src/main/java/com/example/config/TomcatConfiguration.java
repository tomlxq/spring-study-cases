package com.example.config;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.catalina.core.StandardContext;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/31
 */
@Configuration
public class TomcatConfiguration implements EmbeddedServletContainerCustomizer {

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
            // 相当于new TomcatConnectorCustomizer()
            factory.addConnectorCustomizers(connector -> {
                connector.setPort(9090);
                connector.setURIEncoding(StandardCharsets.UTF_8.name());
                connector.setProtocol("HTTP/1.1");
            });
            // factory.setProtocol("HTTP/1.1");
            // factory.setPort(9090);
            // factory.setUriEncoding(StandardCharsets.UTF_8);
        }
    }
}
