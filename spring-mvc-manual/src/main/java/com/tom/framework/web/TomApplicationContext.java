package com.tom.framework.web;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tom.framework.annotation.Autowire;
import com.tom.framework.annotation.Controller;
import com.tom.framework.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TomApplicationContext {

    private List<String> listClass = Lists.newArrayList();
    Properties config = new Properties();
    private Map<String, Object> instanceMapping = new ConcurrentHashMap<String, Object>() {
    };
    private Map<String, String> ctxMap = new ConcurrentHashMap<String, String>() {
    };

    public TomApplicationContext(String location) {
        try {
            //1.定位
            String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.substring(CLASSPATH_ALL_URL_PREFIX.length()));
            if (is == null) {

                throw new FileNotFoundException(location + " cannot be opened because it does not exist");

            }
            //２.加载配置文件
            //Properties config = new Properties();
            try {
                config.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String pack = config.getProperty("base-package");
            //3.注册，仅保存类对象
            //实际spring是BeanDefinition
            doRegister(pack);
            System.out.println("listClass:" + JSON.toJSONString(this.listClass));
            //4.实例化ＩＯＣ对象　加了注解@Controller,@Service,@Repository,@Component的类
            doCreateBeans();
            System.out.println("instanceMapping:" + JSON.toJSONString(this.instanceMapping));
            //5. 依赖注入　将类中的属性加了@Autowire的赋值
            populate();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void populate() {
        if (MapUtils.isEmpty(this.instanceMapping)) return;
        this.instanceMapping.forEach((key, value) -> {
            Field[] fields = value.getClass().getDeclaredFields();
            Arrays.asList(fields).stream().forEach(field -> {
                if (!field.isAnnotationPresent(Autowire.class)) return;
                Autowire annotation = field.getAnnotation(Autowire.class);
                String id = annotation.value().trim();
                //如果id为空，自已没有设值，则根据类型来注入
                if ("".equals(id)) {
                    id = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(value, this.instanceMapping.get(id));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void doCreateBeans() {
        if (CollectionUtils.isEmpty(this.listClass)) {
            return;
        }
        this.listClass.forEach(className -> {
            try {
                Class<?> clazz = Class.forName(className);
                //只有加了@Controller @Service才初始化
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller annotation = clazz.getAnnotation(Controller.class);
                    String id = annotation.value().trim();
                    if ("".equals(id)) {
                        id = lowerFirstLetter(clazz.getSimpleName());
                    }
                    instanceMapping.put(id, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service annotation = clazz.getAnnotation(Service.class);
                    String id = annotation.value().trim();
                    if (!"".equals(id)) {
                        instanceMapping.put(id, clazz.newInstance());
                        return;
                    }
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class c : interfaces) {
                        instanceMapping.put(c.getName(), clazz.newInstance());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public Properties getConfig() {
        return config;
    }

    private String lowerFirstLetter(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doRegister(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        System.out.println(url.getPath());
        File file = new File(url.getPath());
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                doRegister(packageName + "." + f.getName());

            } else {
                listClass.add(packageName + "." + f.getName().replace(".class", ""));
            }
        }

    }


    public Object getBean(String name) {
        return this.instanceMapping.get(name);
    }

    public Map<String, Object> getAll() {
        return this.instanceMapping;
    }
}
