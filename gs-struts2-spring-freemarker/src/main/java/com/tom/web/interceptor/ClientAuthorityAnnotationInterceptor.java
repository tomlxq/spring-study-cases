package com.tom.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.tom.common.SpringContextHolder;


import com.tom.filter.WebConstants;
import com.tom.model.Account;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * User: TOM
 * Date: 2015/9/29
 * email: beauty9235@gmail.com
 * Time: 15:28
 */
public class ClientAuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle ..........");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handler2=(HandlerMethod) handler;
        ClientFireAuthority fireAuthority = handler2.getMethodAnnotation(ClientFireAuthority.class);
        if(null == fireAuthority){
            logger.debug("No statement permissions, release ..........");
            //没有声明权限,放行
            return true;
        }
        boolean isLogged=fireAuthority.isLogged();
        Account account= (Account)WebUtils.getSessionAttribute(request, WebConstants.SESSION_CURRENT_USER);
     //   Location loc = WebUtils.getSessionLocation(request);
        if(account==null){ //trying to auto login for clients
            //CookieService cookieService = SpringContextHolder.getBean("cookieServiceImpl");
            //cookieService.clientAutoLogin(request, response);
            //loc = WebUtils.getSessionLocation(request);
        }
        if(isLogged){  //要求必须登陆
            if(!StringUtils.endsWith( request.getRequestURI(), "/login")&&account==null) {
                throw new Exception("CLIENT_NOT_LOGIN");  //转到全局错误处理页 请参考ApiExceptionHandler.java
            }
             return true;
        }
        return true;
    }
    private void outputURL(HttpServletRequest request, HttpServletResponse response, ClientFireAuthority fireAuthority, String url, String msg) throws IOException {
        if (fireAuthority.resultType() == ResultTypeEnum.page) {
            //传统的登录页面
            StringBuilder sb = new StringBuilder();
            sb.append(request.getContextPath());
            sb.append(url);
            response.sendRedirect(sb.toString());
            logger.debug("You can not access the page: {}", msg);
        }else if(fireAuthority.resultType() == ResultTypeEnum.json){
            //ajax类型的登录提示
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,"utf-8"));
            JSONObject model = new JSONObject();
            model.put("status",false);
            model.put("msg",msg);
            pw.println(model.toString());
            pw.flush();
            pw.close();
        }
    }
}
