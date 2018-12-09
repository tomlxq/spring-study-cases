package com.tom.framework.web;

//import com.sun.istack.internal.Nullable;

import com.google.common.collect.Maps;
import com.tom.framework.annotation.Controller;
import com.tom.framework.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.MapUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TomDispatchServlet extends HttpServlet {

    public static final String CONFIG_LOCATION = "contextConfigLocation";
    private Map<String, HandlerMapping> handlerMapping = Maps.newHashMap();
    private List<HandlerMapping> handlerMappings = null;

    //初始化ＩＣＯ容器
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("系统初始化");
        //初始化ＩＯＣ容空器
        TomApplicationContext context = new TomApplicationContext(config.getInitParameter(CONFIG_LOCATION));

        System.out.println("ＩＯＣ完成初始化");
        //是否有流文件
        initMultipartResolver(context);
        //语言　国际化
        initLocaleResolver(context);
        //主题
        initThemeResolver(context);
        //URL与方法的映射
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
        super.init(config);
    }

    private void initViewResolvers(TomApplicationContext context) {

    }

    private void initFlashMapManager(TomApplicationContext context) {

    }

    private void initRequestToViewNameTranslator(TomApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(TomApplicationContext context) {

    }

    private void initHandlerAdapters(TomApplicationContext context) {

    }

    /**
     * 只解析有controller类
     * 并且只映射有RequestMapping的方法找出来存起了
     *
     * @param context
     */
    private void initHandlerMappings(TomApplicationContext context) {
        Map<String, Object> all = context.getAll();
        if (MapUtils.isEmpty(all)) return;
        all.forEach((key, value) -> {
            Class<?> clazz = value.getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                return;
            }
            String prefix = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                prefix = annotation.value();
            }
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                if (!m.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping annotation = m.getAnnotation(RequestMapping.class);
                String url = prefix += annotation.value();
                handlerMapping.put(url, new HandlerMapping(value, m));
            }

        });

    }

    private void initThemeResolver(TomApplicationContext context) {

    }

    private void initLocaleResolver(TomApplicationContext context) {

    }

    private void initMultipartResolver(TomApplicationContext context) {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        this.doPost(req, resp);
    }

    //调用我们的自己写的Controller
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("调用");
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 inner exception, msg " + Arrays.toString(e.getStackTrace()));
        }
        // super.doPost(req, resp);
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecutionChain mappedHandler = null;
        mappedHandler = getHandler(request);
        if (mappedHandler == null) {
            response.getWriter().write("404 not found");
            return;
        }
        HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
        // Actually invoke the handler.
        ModelAndView mv = ha.handle(request, response, mappedHandler.getHandler());
    }

    private HandlerAdapter getHandlerAdapter(HandlerExecutionChain handler) {
        return null;
    }

    /**
     * 从HandlerMapping中取出来
     *
     * @param request
     * @return
     */
    private HandlerExecutionChain getHandler(HttpServletRequest request) {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    class HandlerAdapter {
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain handler) {
            return null;
        }
    }

    class ModelAndView {
    }

    @AllArgsConstructor
    @NoArgsConstructor
    class HandlerMapping {
        private Object controller;
        private Method method;

        public HandlerExecutionChain getHandler(HttpServletRequest request) {
            return null;
        }
    }

    class HandlerExecutionChain {
        public HandlerExecutionChain getHandler() {
            return null;
        }
    }

}
