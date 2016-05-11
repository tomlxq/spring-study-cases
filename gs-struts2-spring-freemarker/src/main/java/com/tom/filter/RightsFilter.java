package com.tom.filter;

/**
 * Created by tom on 2016/5/11.
 */
import com.tom.common.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RightsFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(RightsFilter.class);
    private String[] arySkipURI = {"/people/login"};
    private String[] suffixURI = {"jsp", "action"};
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(final ServletRequest arg0, final ServletResponse arg1, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) arg0;
        final HttpServletResponse response = (HttpServletResponse) arg1;
        HttpSession session = request.getSession(true);
        //禁止某个IP
    /*    String[] banned_ip= new String[]{ "69.58.178.58",
                "178.255.215.81",
                "144.76.7.107",
                "180.76.15.140",
                "180.76.15.154",
                "180.76.15.137",
                "180.76.15.162",
                "184.154.36.172",
                "184.154.36.173",
                "184.154.36.174",
                "184.154.36.176",
                "184.154.36.175"};
        String ip=WebUtils.toIpAddr(request);
        if(ArrayUtils.contains(banned_ip, ip)){
            logger.info("Your IP {} is prohibited, prohibited access!",ip);
            return;
        }*/

        //RequestURI /ajax/FtpOperate.action
        //request.setCharacterEncoding("UTF-8");
        //response.setCharacterEncoding("UTF-8");
        //response.setContentType("text/html;charset=UTF-8");
        String uri = request.getRequestURI(); //请求入口地址
        String url = request.getRequestURL().toString();
        Pattern pattern = Pattern.compile("/^(.+\\/client\\/.+)|(.+\\.vectologo\\.com.*)|(.+\\.ambiancia\\.com.*)|(.+\\/packweb\\/.+)$/");
        Matcher matcher = pattern.matcher(url);
        String adminLoginUrl=request.getContextPath() + "/services/people/login";
        if (StringUtils.equals(uri, "/")) {
          response.sendRedirect(adminLoginUrl);
        }
        String suffix = FilenameUtils.getExtension(uri);
        // String suffix = FilenameUtils.getExtension(uri);
        if (ArrayUtils.contains(suffixURI, suffix)) { //只对这两种文件作权限检查   *.htm也是action但不需要权限
            if (StringUtils.startsWith(uri, "/admin/"))  //如果是来自客户端的访问
            {     //如果是来自内部人员访问的访问
                if (session.getAttribute(WebConstants.SESSION_CURRENT_USER) == null) {
                    boolean blSkipPage = false;
                    for (int i = 0; i < arySkipURI.length; i++) {
                        if (StringUtils.containsIgnoreCase(uri, arySkipURI[i])) {
                            blSkipPage = true;
                            break;
                        }
                    }
                    if (!blSkipPage) {
                        redirectLogin(request, response);
                        return;
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void redirectLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUri = request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        if (StringUtils.isNotEmpty(requestUri) && StringUtils.contains(requestUri, "/services/people/logout")) {
            requestUri = "";
        }
        response.sendRedirect(request.getContextPath() + "/services/people/login?login_referer=" + StringUtil.base64Encode(requestUri));
    }


    public void destroy() {
    }
}
