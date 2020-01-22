package com.tom.tomcat.server;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/18
 */
public class NettyRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest httpRequest;

    public NettyRequest(ChannelHandlerContext ctx, HttpRequest httpRequest) {
        this.ctx = ctx;
        this.httpRequest = httpRequest;
    }

    public String getUri() {
        return this.httpRequest.uri();
    }

    public String getMethod() {
        return this.httpRequest.method().name();
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(getUri());
        return queryStringDecoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        List<String> list = parameters.get(name);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
