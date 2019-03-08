package com.tom;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("pay-service-consumer.xml");
        IPayService payServices = (IPayService) context.getBean("payServices");
        payServices.payMoney();
        Protocol myProtocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("myProtocol");
        System.out.println(myProtocol.getDefaultPort());
        System.in.read();
    }
}
