package com.tom.catalina.servlet;

import com.tom.catalina.http.TomHttpRequest;
import com.tom.catalina.http.TomHttpResponse;
import com.tom.catalina.http.TomServlet;

public class MyServlet extends TomServlet {
    @Override
    public void doGet(TomHttpRequest req, TomHttpResponse res) {
        this.doPost(req, res);
    }

    @Override
    public void doPost(TomHttpRequest req, TomHttpResponse res) {
        res.write(req.getParameter("name"));
    }
}
