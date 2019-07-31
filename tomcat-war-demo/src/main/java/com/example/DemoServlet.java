package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link DemoServlet} servlet 3.1.0 写法
 *
 * @author TomLuo
 * @date 2019/7/28
 */
@WebServlet(urlPatterns = "/demo")
public class DemoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setContentType("text/html;charset:UTF-8");
        String message = req.getParameter("message");
        resp.getWriter().println(message);
        String contextPath = req.getServletContext().getContextPath();
        resp.getWriter().println("contextPath :" + contextPath);
    }
}
