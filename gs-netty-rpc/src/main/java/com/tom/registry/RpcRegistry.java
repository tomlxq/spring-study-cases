package com.tom.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcRegistry {
    int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start() {
        // 服务器线程组 用于网络事件的处理 一个用于服务器接收客户端的连接
        // 另一个线程组用于处理SocketChannel的网络读写
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            //NIO服务器端的辅助启动类 降低服务器开发难度
            ServerBootstrap server = new ServerBootstrap();
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)// 配置TCP参数
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //处理拆包，粘包的编解码器
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            //处理序列化的编、解码器(JDK默认)
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast("encoder", new ObjectEncoder());
                            //实现业务逻辑
                            pipeline.addLast(new MyRegistryHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
// 最后绑定I/O事件的处理类
            // 服务器启动后 绑定监听端口 同步等待成功 主要用于异步操作的通知回调 回调处理用的ChildChannelHandler

            ChannelFuture f = server.bind(this.port).sync();
            System.out.println("注册中心服务开始，监听端口为：" + this.port);
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出 释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            System.out.println("服务器优雅的释放了线程资源...");
        }
    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}
