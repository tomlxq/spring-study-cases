package com.tom.consumer.proxy;

import com.tom.msg.InvokerMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxy {


    public <T> T create(Class<?> clazz) {
        MethodProxy methodProxy = new MethodProxy(clazz);
        T ret = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, methodProxy);
        return ret;
    }

    private class MethodProxy implements InvocationHandler {
        Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果传进来的是一个实现类，则直接发起调用
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else {//如果传进来的是一个接口，则发起远程调用
                return rpcInvoke(method, args);
            }

        }

        public Object rpcInvoke(Method method, Object[] args) {
            InvokerMsg msg = new InvokerMsg();
            msg.setClazzName(this.clazz.getName());
            msg.setMethod(method.getName());
            msg.setParams(method.getParameterTypes());
            msg.setValues(args);
            //配置客户端nio线程组
            EventLoopGroup group = new NioEventLoopGroup();
            final MyProxyHandler proxyHandler = new MyProxyHandler();
            try {
                //客户端辅助启动类 对客户端配置
                Bootstrap client = new Bootstrap();
                client.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                //处理拆包，粘包的编解码器
                                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
                                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                                //处理序列化的编、解码器(JDK默认)
                                pipeline.addLast("encoder", new ObjectEncoder());
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                //实现业务逻辑
                                pipeline.addLast(proxyHandler);
                            }
                        });
                //异步链接服务器 同步等待链接成功
                ChannelFuture f = client.connect("127.0.0.1", 8080).sync();
                f.channel().writeAndFlush(msg).sync();
                //等待链接关闭
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
                System.out.println("客户端优雅的释放了线程资源...");
            }
            return proxyHandler.getResult();
        }
    }
}
