package com.tom.tomcat.server;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/18
 */
public abstract class NettyServlet {
    public abstract void get(NettyRequest request, NettyResponse response);

    public abstract void post(NettyRequest request, NettyResponse response);
}
