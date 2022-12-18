package com.tom.tomcat.server;

import com.tom.tomcat.servlet.MyServlet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/18
 */
public class MyTomcatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest)msg;
            NettyRequest nettyRequest = new NettyRequest(ctx, httpRequest);
            NettyResponse nettyResponse = new NettyResponse(ctx, httpRequest);
            MyServlet servlet = new MyServlet();
            servlet.get(nettyRequest, nettyResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
