package com.tom.framework.web;

//import com.sun.istack.internal.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tom.framework.annotation.Controller;
import com.tom.framework.annotation.RequestMapping;
import com.tom.framework.annotation.RequestPara;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class TomDispatchServlet extends HttpServlet {

    public static final String CONFIG_LOCATION = "contextConfigLocation";
    List<ViewResolver> viewResolvers = Lists.newArrayList();
    private Map<Pattern, HandlerMapping> handlerMapping = Maps.newHashMap();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = Maps.newHashMap();

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

        String template = context.getConfig().getProperty("template");
        String dir = this.getClass().getClassLoader().getResource(template).getFile();
        //  String  dir="F:\\data\\wwwtest\\spring-study-cases\\spring-mvc-manual\\src\\main\\webapp\\WEB-INF\\"+template;
        File file = new File(dir);
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                viewResolvers.add(new ViewResolver(f.getName(), f));
            }
        }
    }

    private void initFlashMapManager(TomApplicationContext context) {

    }

    private void initRequestToViewNameTranslator(TomApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(TomApplicationContext context) {

    }

    /**
     * 动态匹配方法里的参数
     *
     * @param context
     */
    private void initHandlerAdapters(TomApplicationContext context) {
        if (MapUtils.isEmpty(this.handlerMapping)) return;
        Map<String, Integer> paraType = Maps.newHashMap();
        this.handlerMapping.forEach((key, value) -> {
            Class<?>[] parameterTypes = value.getMethod().getParameterTypes();
            IntStream.range(0, parameterTypes.length).forEach(i -> {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                    paraType.put(parameterType.getName(), i);
                }
            });
            Annotation[][] parameterAnnotations = value.getMethod().getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof RequestPara) {
                        String paraName = ((RequestPara) annotation).value().trim();
                        if (!"".equals(paraName)) {
                            paraType.put(paraName, i);
                        }
                    }
                }
            }
            handlerAdapters.put(value, new HandlerAdapter(paraType));
        });
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
                String regex = (prefix + annotation.value()).replaceAll("/+", "/");
                System.out.println(regex + " " + m.toString());
                Pattern pattern = Pattern.compile(regex);
                handlerMapping.put(pattern, new HandlerMapping(value, m));
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
        //HandlerExecutionChain mappedHandler = null;
        HandlerMapping mappedHandler = getHandler(request);
        if (mappedHandler == null) {
            response.getWriter().write("404 not found");
            return;
        }
        HandlerAdapter ha = getHandlerAdapter(mappedHandler);
        // Actually invoke the handler.
        ModelAndView mv = ha.handle(request, response, mappedHandler);
        applyDefaultViewName(response, mv);
    }

    private void applyDefaultViewName(HttpServletResponse response, ModelAndView mv) {
        if (null == mv) {
            return;
        }
        String viewName = mv.getViewName();
        if (CollectionUtils.isEmpty(viewResolvers)) {
            return;
        }
        viewResolvers.forEach(viewResolver -> {
            if (!StringUtils.equals(viewResolver.getView(), viewName)) return;
            // ViewResolver viewResolver = viewResolvers.get(viewName);
            String html = viewResolver.parse(mv);
            try {
                response.getWriter().write(html);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        });


    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        return this.handlerAdapters.get(handler);
    }

    /**
     * 从HandlerMapping中取出来
     *
     * @param request
     * @return
     */
    private HandlerMapping getHandler(HttpServletRequest request) {
        if (MapUtils.isEmpty(this.handlerMapping)) return null;
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String url = requestURI.replace(contextPath, "").replaceAll("/+", "/");
        for (Pattern entry : this.handlerMapping.keySet()) {
            Matcher matcher = entry.matcher(url);
            if (!matcher.find()) {
                continue;
            }
            return this.handlerMapping.get(entry);

        }
        // return this.handlerMapping.get(url);
        //System.out.println("aaaa:"+contextPath);
        //System.out.println("bbbb:"+requestURI);
        //System.out.println(this.handlerMapping);;
       /* if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }*/
        return null;
    }

    private Object castStringValue(String v, Class<?> parameterType) {
        if (parameterType == String.class) {
            return v;
        } else if (parameterType == Integer.class) {
            return Integer.valueOf(v);
        } else if (parameterType == int.class) {
            return Integer.valueOf(v);
        }
        return null;
    }

    @Getter
    class HandlerAdapter {
        private Map<String, Integer> adapterMap = Maps.newHashMap();

        public HandlerAdapter(Map<String, Integer> adapterMap) {
            this.adapterMap = adapterMap;
        }

        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMapping handler) throws InvocationTargetException, IllegalAccessException {
            Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
            Object[] paraValues = new Object[parameterTypes.length];
            Map<String, String[]> parameterMap = request.getParameterMap();
            parameterMap.forEach((key, value) -> {
                if (!this.adapterMap.containsKey(key)) return;
                String v = Arrays.toString(value).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                int index = this.adapterMap.get(key);
                paraValues[index] = castStringValue(v, parameterTypes[index]);
                String name = HttpServletRequest.class.getName();
                if (this.getAdapterMap().containsKey(name)) {
                    int reqIdx = this.getAdapterMap().get(name);
                    paraValues[reqIdx] = request;
                }
                name = HttpServletResponse.class.getName();
                if (this.getAdapterMap().containsKey(name)) {
                    int resIdx = this.getAdapterMap().get(name);
                    paraValues[resIdx] = response;
                }
            });
            if (handler.method.getReturnType() == ModelAndView.class) {
                return (ModelAndView) handler.method.invoke(handler.controller, paraValues);
            } else {
                return null;
            }
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
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
