package com.tom.server;

import com.tom.server.handler.HttpHandler;
import com.tom.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServer {
    /**
     * 默认端口
     */
    int port = 8080;

    public static void main(String[] args) {
        new ChatServer().start();
    }

    private void start() {
        //
        //boss线程
        EventLoopGroup boss = new NioEventLoopGroup();
        //Worker线程
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            //服务引擎
            ServerBootstrap server = new ServerBootstrap();
            //netty主从模型
            server.group(boss, worker)
                    //设置主线程 线程池为128
                    .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    //设置子线程
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            //业务逻辑处理
                            ChannelPipeline pipeline = client.pipeline();
                            /**
                             * 支持http协议
                             */
                            /**
                             * 编码与解码http请求的
                             */
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * 用来拦截http请求的
                             */
                            pipeline.addLast(new HttpHandler());
                            //支持WebSocket
                            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                            pipeline.addLast(new WebSocketHandler());
                        }
                    }).childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = server.bind(this.port).sync();
            System.out.println("netty服务器启动了，端口为" + this.port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
