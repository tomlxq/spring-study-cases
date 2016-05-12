package com.tom.web;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * User: TOM
 * Date: 2016/5/12
 * email: beauty9235@gmail.com
 * Time: 11:06
 */
public class BaseAction  extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {
    public final static Logger logger = LoggerFactory.getLogger(BaseAction.class);
    String accessRightId;  //权限位
    protected Map model = new HashMap();//freemarker数据模型
    public HttpServletRequest request;
    public HttpServletResponse response;
    @SuppressWarnings("unchecked")
    public Map session;

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
