package com.tom.web.exception;

/**
 * Created by tom on 2016/5/12.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * User: TOM
 * Date: 2015/10/9
 * email: beauty9235@gmail.com
 * Time: 15:21
 */

public class MyExceptionHandler implements HandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("ex", ex);

        logger.info("{}",ex);
        // 根据不同错误转向不同页面
        return new ModelAndView("/global/error", model);
    }
}