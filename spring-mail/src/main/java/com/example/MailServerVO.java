package com.example;

/**
 * Created by tom on 2016/5/10.
 */
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Properties;

public class MailServerVO extends ResourceSupport implements Serializable {
    private String host;
    private Integer port;
    private String userName;
    private String password;
    private String defaultEncoding;
    private Properties properties;

    public MailServerVO() {}

    public MailServerVO(
            String host,
            Integer port,
            String userName,
            String password,
            String defaultEncoding,
            Properties properties) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.defaultEncoding = defaultEncoding;
        this.properties = properties;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "MailServerVO{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", defaultEncoding='" + defaultEncoding + '\'' +
                ", properties=" + properties +
                "} ";
    }
}
