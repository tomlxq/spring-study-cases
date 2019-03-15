package com.tom.server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    URL baseUrl = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    static final String WEB_ROOT = "webRoot";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        String uri = req.getUri();
        String page = uri.equals("/") ? "chat.html" : uri;
        String contextType = "text/html";
        if (uri.endsWith(".css")) {
            contextType = "text/css";
        } else if (uri.endsWith(".js")) {
            contextType = "text/javascript";
        } else if (uri.toLowerCase().matches("(jpg|gif|png|icon)$")) {
            final String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        System.out.println(contextType);
        RandomAccessFile file = new RandomAccessFile(getFile(page), "r");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        res.headers().set(HttpHeaders.Names.CONTENT_TYPE, contextType + ";charset:utf-8;");
        final boolean keepAlive = HttpHeaders.isKeepAlive(req);
        if (keepAlive) {
            res.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            res.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(res);
        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }

    private File getFile(String page) throws URISyntaxException {
        String path = baseUrl.toURI() + WEB_ROOT + "/" + page;
        path = path.contains("file:/") ? path.substring(5) : path;
        return new File(path);
    }
}
