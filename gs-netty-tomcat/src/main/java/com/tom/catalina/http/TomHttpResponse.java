package com.tom.catalina.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.EXPIRES;

public class TomHttpResponse {
    private ChannelHandlerContext ctx;
    private HttpRequest r;

    public TomHttpResponse(ChannelHandlerContext ctx, HttpRequest r) {
        this.ctx = ctx;
        this.r = r;
    }

    public void write(String out) {
        try {
            if (out == null || out.equals("")) out = "";
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes(Charset.forName("UTF-8"))));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(EXPIRES, 0);
            if (HttpHeaders.isKeepAlive(r)) {
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);
        } finally {
            ctx.flush();
        }
    }
}
