package com.tom.tomcat.servlet;

import com.tom.tomcat.server.NettyRequest;
import com.tom.tomcat.server.NettyResponse;
import com.tom.tomcat.server.NettyServlet;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2020/1/18
 */
public class MyServlet extends NettyServlet {
    @Override
    public void get(NettyRequest request, NettyResponse response) {
        response.write(request.getParameter("name"));

    }

    @Override
    public void post(NettyRequest request, NettyResponse response) {
        this.get(request, response);
    }
}
