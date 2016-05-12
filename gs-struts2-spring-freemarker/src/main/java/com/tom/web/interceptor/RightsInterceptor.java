package com.tom.web.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.tom.filter.WebConstants;
import com.tom.model.Account;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 10:56
 */
public class RightsInterceptor implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(RightsInterceptor.class);

    public void destroy() {
    }

    public void init() {

    }

    public String intercept(ActionInvocation actionInvocation) throws Exception {
        ActionContext ctx = actionInvocation.getInvocationContext();
        String accessRightId = (String) ctx.getValueStack().findValue("accessRightId");
        HttpServletResponse response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        Map map = ctx.getSession();
        List ar = (List) MapUtils.getObject(map, WebConstants.SESSION_ACCESS_RIGHT_LIST);
        Account account =(Account)MapUtils.getObject(map, WebConstants.SESSION_CURRENT_USER);
        if (account == null) {
            throw new Exception("NOT_LOG_IN");
        } else if (StringUtils.endsWithIgnoreCase(account.getRole(), WebConstants.SA)) {
            return actionInvocation.invoke();
        } else if (StringUtils.isEmpty(accessRightId)) {
            throw new Exception("RIGHT_ID_EMPTY");
        } else if (!ar.contains(accessRightId)) {
            throw new Exception("NO_ACCESS_RIGHT");
        } else {
            return actionInvocation.invoke();
        }

    }
}
