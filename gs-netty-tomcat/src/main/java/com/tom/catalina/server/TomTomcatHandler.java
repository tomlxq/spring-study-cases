package com.tom.catalina.server;

import com.tom.catalina.http.TomHttpRequest;
import com.tom.catalina.http.TomHttpResponse;
import com.tom.catalina.servlet.MyServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

public class TomTomcatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest r = (HttpRequest) msg;
            TomHttpRequest request = new TomHttpRequest(ctx, r);
            TomHttpResponse response = new TomHttpResponse(ctx, r);
            new MyServlet().doGet(request, response);
        }
        //super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

