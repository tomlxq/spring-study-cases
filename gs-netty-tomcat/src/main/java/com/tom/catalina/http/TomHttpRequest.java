package com.tom.catalina.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class TomHttpRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest r;

    public TomHttpRequest(ChannelHandlerContext ctx, HttpRequest r) {
        this.ctx = ctx;
        this.r = r;
    }

    public String getUri() {
        return r.getUri();
    }

    public String getMethod() {
        return r.getMethod().name();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(r.uri());
        return queryStringDecoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        List<String> para = parameters.get(name);
        if (para == null || para.isEmpty()) {
            return null;
        }
        return para.get(0);
    }
}
