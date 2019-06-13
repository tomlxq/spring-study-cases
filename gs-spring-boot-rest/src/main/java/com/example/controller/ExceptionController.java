package com.example.controller;

import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NullPointerException.class)
    public Object errorPage(HttpStatus httpStatus, HttpServletRequest request, Throwable throwable) {
        Map<String, Object> map = Maps.newHashMap();

        map.put("status_code", request.getAttribute("javax.servlet.error.status_code"));
        map.put("exception_type", request.getAttribute("javax.servlet.error.exception_type"));
        map.put("message", request.getAttribute("javax.servlet.error.message"));
        map.put("exception", request.getAttribute("javax.servlet.error.exception"));
        map.put("request_uri", request.getAttribute("javax.servlet.error.request_uri"));
        map.put("servlet_name", request.getAttribute("javax.servlet.error.servlet_name"));
        return map;
    }


}
