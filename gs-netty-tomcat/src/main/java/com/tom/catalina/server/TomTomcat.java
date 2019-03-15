package com.tom.catalina.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class TomTomcat {
    public static void main(String[] args) {
        new TomTomcat().start(8080);
    }

    private void start(int port) {
        //bio
        //ServerSocketChannel open = ServerSocketChannel.open();
        //open.bind(local);
        //nio
        //ServerSocket serverSocket = new ServerSocket();
        try {
            //boss threads
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            //worker threads
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap server = new ServerBootstrap();
                server.group(bossGroup, workerGroup)
                        //主线程的处理类
                        .channel(NioServerSocketChannel.class)
                        //子线程的处理类，是一个个HandlerAdapter
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel client) throws Exception {
                                //无锁化串行编程
                                //业务逻辑链路 编码器
                                client.pipeline().addLast(new HttpResponseEncoder());
                                //解码器
                                client.pipeline().addLast(new HttpRequestDecoder());
                                //实现业务逻辑
                                client.pipeline().addLast(new TomTomcatHandler());
                            }
                        })
                        //主线程的配置项
                        .option(ChannelOption.SO_BACKLOG, 128)
                        //子线程的配置项
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                ChannelFuture channelFuture = server.bind(port).sync();
                System.out.println("tom tomcat已启动" + port);
                channelFuture.channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
