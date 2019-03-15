package com.tom.catalina.http;

public abstract class TomServlet {
    public abstract void doGet(TomHttpRequest req, TomHttpResponse res);

    public abstract void doPost(TomHttpRequest req, TomHttpResponse res);
}
