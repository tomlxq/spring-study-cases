package com.tom.web.interceptor;
import com.alibaba.fastjson.JSONObject;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * User: TOM
 * all spring rest API access right controller
 * Date: 2015/1/17
 * email: beauty9235@gmail.com
 * Time: 15:22
 */
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("preHandle ..........");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handler2=(HandlerMethod) handler;
        FireAuthority fireAuthority = handler2.getMethodAnnotation(FireAuthority.class);
        if(null == fireAuthority){
            logger.debug("No statement permissions, release ..........");
            //没有声明权限,放行
            return true;
        }
        String accessRightId=fireAuthority.accessRightId();
        logger.debug("fireAuthority {} ", accessRightId);
        HttpSession session = request.getSession(true);
        Account account= (Account) WebUtils.getSessionAttribute(request, WebConstants.SESSION_CURRENT_USER);
        List ar = (List) session.getAttribute(WebConstants.SESSION_ACCESS_RIGHT_LIST);


      //  String requestUri = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        if (account == null) {
            throw new Exception("NOT_LOG_IN");
          //转到全局错误处理页
        }else if (StringUtils.equals(account.getRole(), WebConstants.SA)) {
            return true;
        }else if (StringUtils.isEmpty(accessRightId)) {
            throw new Exception("NO_ACCESS_RIGHT");
        } else if ( !ar.contains(accessRightId)) {
            throw new Exception("NO_ACCESS_RIGHT");
        }  else {
            return true;
        }
    }
    private void outputURL(HttpServletRequest request, HttpServletResponse response, FireAuthority fireAuthority, String url, String msg) throws IOException {
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
