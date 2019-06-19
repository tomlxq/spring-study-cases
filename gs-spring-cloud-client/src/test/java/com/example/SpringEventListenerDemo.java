package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 功能描述
 *
 * @author hp
 * @email 72719046@qq.com
 * @date 2019/6/16
 */
public class SpringEventListenerDemo {
    public static void main(String[] args) {
        // Annotation驱动
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册事件监听器
        context.addApplicationListener(new ApplicationListener<MySpringEvent>() {
            @Override
            public void onApplicationEvent(MySpringEvent event) {
                System.out.println(event.getSource() + " @ " + event.context);
            }
        });
        // 初始化context
        context.refresh();
        // 发布事件
        context.publishEvent(new MySpringEvent(context, "hello,world"));
        context.publishEvent(new MySpringEvent(context, "this is first email"));
        context.publishEvent(new MySpringEvent(context, 1));

    }

    public static class MySpringEvent extends ApplicationEvent {
        ApplicationContext context;

        /**
         * Create a new ApplicationEvent.
         *
         * @param source
         *            the object on which the event initially occurred (never {@code null})
         */
        public MySpringEvent(ApplicationContext context, Object source) {

            super(source);
            this.context = context;
        }

    }
}
