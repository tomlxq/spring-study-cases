package com.tom.server.handler;

import com.tom.protocal.IMProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private IMProcessor processor = new IMProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        processor.process(ctx.channel(), msg.text());
        //System.out.println(msg.text());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        processor.logout(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }
}
