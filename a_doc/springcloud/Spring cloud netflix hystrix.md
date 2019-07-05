# Spring cloud netflix hystrix

> https://github.com/Netflix/Hystrix
>
> 容错，短路
>
> https://martinfowler.com/bliki/CircuitBreaker.html 
>
> QPS Query per second
>
> 经过全链路(一个完整的业务流程)压测，计算单机极限QPS 
>
> `集群QPS=单机QPS*集群机器数量*可靠性比率`
>
> TPS Transaction per second
>
>  JMeter 可调整性比较灵活

## 服务短路（circuitBreaker)

## Spring client Hystrix Client

官网： https://github.com/Netflix/Hystrix

> Reactive Java 框架 
>
> https://reactive.io/
>
> * Java 9 Flow API
> * Reactor
> * RxJava （Reactive X）
>   ExecutorService、CompletionService、CompletableFuture

> 配置信息的WIKI ： https://github.com/Netflix/Hystrix/wiki/Configuration

```java
java.util.concurrent.locks.AbstractQueuedSynchronizer#acquire 与LOCK相似
java.util.concurrent.locks.AbstractQueuedSynchronizer#release   与UNLOCK相似
```

激活Hystrix

`@EnableHystri`

### Hystrix(注解的方式Annotation)

```java
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
}
```

### Hystrix(编程方式)

```java
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
```



对比其它方式

### Feature

```java
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
```

### Reactive X

```java
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
```

### Health Endpoint(`/health`)

```xml 
{
    status: "UP",
    details: {
        diskSpace: {
            status: "UP",
            details: {
                total: 997041631232,
                free: 930767966208,
                threshold: 10485760
            }
        },
        refreshScope: {
            status: "UP"
        },
        hystrix: {
            status: "UP"
        }
    }
}
```

### Hystrix Endpoint(`/hystrix.stream`)

激活熔断保护

`@EnableCircuitBreaker`激活`@EnableHystrix`+Spring Cloud功能

`@EnableHystrix`没有Spring Cloud功能,如 `/hystrix.stream`端点

```json
data: {"type":"HystrixThreadPool","name":"HystrixDemoController","currentTime":1561869413729,"currentActiveCount":0,"currentCompletedTaskCount":6,"currentCorePoolSize":10,"currentLargestPoolSize":6,"currentMaximumPoolSize":10,"currentPoolSize":6,"currentQueueSize":0,"currentTaskCount":6,"rollingCountThreadsExecuted":5,"rollingMaxActiveThreads":1,"rollingCountCommandRejections":0,"propertyValue_queueSizeRejectionThreshold":5,"propertyValue_metricsRollingStatisticalWindowInMilliseconds":10000,"reportingHosts":1}
```



## Spring Client Hystrix Dashboard

激活`@EnableHystrixDashboard`

```java
@SpringBootApplication
@EnableHystrixDashboard
public class GsSpringCloudHystrixDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(GsSpringCloudHystrixDashboardApplication.class, args);
    }
}
```

## 整合Netflix Turbine

