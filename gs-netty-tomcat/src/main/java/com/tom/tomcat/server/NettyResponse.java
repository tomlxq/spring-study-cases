package com.tom.tomcat.server;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCEPT_CHARSET;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.EXPIRES;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/18
 */
public class NettyResponse {
    private ChannelHandlerContext ctx;
    private HttpRequest httpRequest;

    public NettyResponse(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.ctx = ctx;
        this.httpRequest = httpRequest;
    }

    public void write(String out) {
        if (StringUtils.isEmpty(out)) {
            return;
        }
        // ByteBuf
        FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
            Unpooled.wrappedBuffer(out.getBytes(StandardCharsets.UTF_8)));
        res.headers().set(CONTENT_LENGTH, res.content().readableBytes());
        res.headers().set(EXPIRES, 0);
        res.headers().set(ACCEPT_CHARSET, StandardCharsets.UTF_8);
        res.headers().set(CONTENT_TYPE, "text/html");
        if (HttpUtil.isKeepAlive(httpRequest)) {
            res.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(res);
        ctx.flush();
    }
}
