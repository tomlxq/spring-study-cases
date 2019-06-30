package com.example;

import static com.example.controller.HystrixDemoController.random;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import rx.Observer;
import rx.Single;
import rx.schedulers.Schedulers;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GsSpringCloudHystrixApplicationTests {

    @Test
    public void contextLoads() {
        Single.just("hello,world").// just 发布的数据
            subscribeOn(Schedulers.immediate()).subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {// 正常流程
                    System.out.println("执行结束");
                }

                @Override
                public void onError(Throwable throwable) {// 异常流程（结束）
                    System.out.println("服务熔断保护");
                }

                @Override
                public void onNext(String s) {// 数据消费 s="hello,world"
                    int time = random.nextInt(200);
                    // 如果随机是时间>100,触发容错
                    System.out.println("helloWorld() execute cost " + time + " ms.");
                    if (time > 100) {
                        throw new RuntimeException();
                    }
                    System.out.println(s);
                }
            });
    }

    private void testTimeout() {
        Random random = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> futures = executorService.submit(() -> {
            int time = random.nextInt(200);
            // 如果随机是时间>100,触发容错
            System.out.println("helloWorld() execute cost " + time + " ms.");
            Thread.sleep(time);
            return "Hello,world!";
        });
        try {
            futures.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("超时保护");

        }
    }

}
