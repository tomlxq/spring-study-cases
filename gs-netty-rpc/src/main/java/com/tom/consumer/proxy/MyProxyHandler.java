package com.tom.consumer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyProxyHandler extends ChannelInboundHandlerAdapter {
    private Object ret;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ret = msg;
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public Object getResult() {
        return this.ret;
    }
}
