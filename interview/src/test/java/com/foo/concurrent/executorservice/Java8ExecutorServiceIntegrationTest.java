package com.foo.concurrent.executorservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @ClassName: Java8ExecutorServiceIntegrationTest
 * @Description:【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 说明：Executors返回的线程池对象的弊端如下：
 * 1）FixedThreadPool和SingleThreadPool: 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 * 2）CachedThreadPool和ScheduledThreadPool: 允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 * @Author: tomluo
 * @Date: 2022/12/17 16:05
 **/
public class Java8ExecutorServiceIntegrationTest {
    private Runnable runnableTask;
    private Callable<String> callableTask;
    private List<Callable<String>> callableTasks;

    @Before
    public void init() {

        runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "Task's execution";
        };

        callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
    }

    private List<Runnable> smartShutdown(ExecutorService executorService) {

        List<Runnable> notExecutedTasks = new ArrayList<>();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                notExecutedTasks = executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            notExecutedTasks = executorService.shutdownNow();
        }
        return notExecutedTasks;
    }

    @Test
    public void creationSubmittingTasksShuttingDownNow_whenShutDownAfterAwating_thenCorrect() {

        ExecutorService threadPoolExecutor =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(callableTask);
        }

        List<Runnable> notExecutedTasks = smartShutdown(threadPoolExecutor);

        assertTrue(threadPoolExecutor.isShutdown());
        assertFalse(notExecutedTasks.isEmpty());
        assertTrue(notExecutedTasks.size() < 98);
    }
}