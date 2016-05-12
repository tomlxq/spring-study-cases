package com.tom.web.interceptor;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 11:01
 */

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.tom.filter.WebConstants;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClientInterceptor implements Interceptor {
    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        ActionContext ctx = actionInvocation.getInvocationContext();

        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(WebConstants.SESSION_CURRENT_USER) == null) {
            //当session为空自动进入　由cookies查询持久层进行自行登陆
            ApplicationContext applicationContext = (ApplicationContext) ctx.getContext().getApplication().get(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            /*这儿可以具体实现登陆*/
           //applicationContext.getBean("myCookieLogin");
            //cookieLogin.autoLogin(request, response);
        }
        return actionInvocation.invoke();
    }
}
