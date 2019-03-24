package com.tom.registry;

import com.google.common.collect.Lists;
import com.tom.msg.InvokerMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyRegistryHandler extends ChannelInboundHandlerAdapter {
    //在注册中心注册需要一个容器来存放
    private ConcurrentHashMap<String, Object> registryNap = new ConcurrentHashMap<String, Object>();
    //class的缓存
    private List<String> classCache = Lists.newArrayList();


    public MyRegistryHandler() {
        doScanPackage("com.tom.provider");
        doRegister();
    }

    /**
     * 实例化并放在容器中registryNap
     */
    private void doRegister() {
        if (CollectionUtils.isEmpty(this.classCache)) {
            return;
        }
        for (String className : classCache) {
            try {
                Class<?> clazz = Class.forName(className);
                //取得接口，假设我们就一个接口
                Class<?> interfaces = clazz.getInterfaces()[0];
                //用接口名字作为容器的key
                registryNap.put(interfaces.getName(), clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        System.out.println(registryNap);

    }

    //扫描文件夹
    private void doScanPackage(String packageName) {
        URL resource = this.getClass().getClassLoader().getResource(packageName.replace(".", "/"));
        File file = new File(resource.getFile());
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                doScanPackage(packageName + "." + f.getName());
            } else {
                classCache.add(packageName + "." + f.getName().replace(".class", "").trim());

            }

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object ret = new Object();
        //设置封装信息
        InvokerMsg request = (InvokerMsg) msg;
        if (registryNap.contains(request.getClazzName())) {
            Object clazz = registryNap.get(request.getClazzName());
            Method method = clazz.getClass().getMethod(request.getClazzName(), request.getParams());
            ret = method.invoke(clazz, request.getValues());
        }
        ctx.writeAndFlush(ret);

        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
