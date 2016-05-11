package com.tom.filter;

/**
 * Created by tom on 2016/5/11.
 */
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MyFilterDispatcher extends StrutsPrepareAndExecuteFilter {
    Logger logger = LoggerFactory.getLogger(MyFilterDispatcher.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        if(!path.contains("/services/")) {
            super.doFilter(request, response, chain);//做struts的事情
        }else {
            chain.doFilter(request, response);//跳过struts

        }
    }

}