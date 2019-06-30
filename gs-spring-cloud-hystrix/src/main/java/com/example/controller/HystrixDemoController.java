package com.example.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * 功能描述
 *
 * @author TomLuo
 * @email 72719046@qq.com
 * @date 2019/6/30
 */
@RestController
public class HystrixDemoController {
    /**
     * 当{@link #helloWorld()} 方法调用超时或失败时，fallbackMethod方法{@link #errContent()}就会作为替找返回
     * 
     * @return
     */
    public static final Random random = new Random();

    @GetMapping("/helloWorld")
    @HystrixCommand(fallbackMethod = "errContent", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100")})
    public String helloWorld() throws InterruptedException {
        int time = random.nextInt(200);
        // 如果随机是时间>100,触发容错
        System.out.println("helloWorld() execute cost " + time + " ms.");
        Thread.sleep(time);

        return "hello,world";
    }

    public String errContent() {
        return "Api invoke failed";
    }

    @GetMapping("/helloWorld2")
    public String helloWorld2() {
        return new MyHystrixCommand().execute();
    }

    private class MyHystrixCommand extends com.netflix.hystrix.HystrixCommand<String> {
        protected MyHystrixCommand() {
            // super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Hello,world")).andCommandPropertiesDefaults(
            // HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100)));
            super(HystrixCommandGroupKey.Factory.asKey("Hello,world"), 100);
        }

        @Override
        protected String run() throws Exception {
            int time = random.nextInt(200);
            // 如果随机是时间>100,触发容错
            System.out.println("helloWorld() execute cost " + time + " ms.");
            Thread.sleep(time);

            return "hello,world";

        }

        @Override
        protected String getFallback() {
            return HystrixDemoController.this.errContent();
        }
    }

}
