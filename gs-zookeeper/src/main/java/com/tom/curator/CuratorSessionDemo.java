package com.tom.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CuratorSessionDemo {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";
    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        //创建会话的两种方式
        CuratorFramework curatorFramework1 =
                CuratorFrameworkFactory.newClient(CONNECTIONS, 5000, 2000,
                        new ExponentialBackoffRetry(1000, 3));
        curatorFramework1.start();
        System.out.println("创建会话成功 -> " + curatorFramework1.getState());
        //删除节点
        curatorFramework1.delete().deletingChildrenIfNeeded().forPath("/curator1");
        //创建节点
        String node = curatorFramework1.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/curator1", "12345".getBytes());
        System.out.println("节点名称 -> " + node);
        //创建有名字空间的会话
        CuratorFramework curatorFramework2 = CuratorFrameworkFactory.builder().connectString(CONNECTIONS).sessionTimeoutMs(5000).connectionTimeoutMs(2000)
                .retryPolicy(new RetryNTimes(10, 1000)).namespace("curator1").build();

        System.out.println("创建会话成功 -> " + curatorFramework2.getState());
        curatorFramework2.start();
        //创建节点
        node = curatorFramework2.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/curator1-1/curator1-1-1", "华为".getBytes());
        System.out.println("节点名称 -> " + node);
        //获取数据
        Stat stat = new Stat();
        byte[] bytes = curatorFramework2.getData().storingStatIn(stat).forPath("/curator1-1/curator1-1-1");
        System.out.println("获取数据 -> " + new String(bytes, UTF8) + "\r\n state -> " + stat);
        //更新数据
        stat = curatorFramework2.setData().forPath("/curator1-1/curator1-1-1", "深圳坂田".getBytes(UTF8));
        System.out.println("设置数据 state -> " + stat);
        //异步操作
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        curatorFramework2.create().creatingParentContainersIfNeeded().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                Stat stat = new Stat();
                byte[] bytes1 = curatorFramework.getData().storingStatIn(stat).forPath("/curator2-2/curator2-2-1");
                System.out.println("线程名称：" + Thread.currentThread().getName() +
                        "\r\n事件类型：" + curatorEvent.getType() + "\r\n事件返回码：" + curatorEvent.getResultCode() +
                        "\r\n数据：" + new String(bytes1, UTF8)
                );
                countDownLatch.countDown();
            }
        }, executorService).forPath("/curator2-2/curator2-2-1", "天安云谷".getBytes(UTF8));
        countDownLatch.await();
        executorService.shutdown();
        //事务操作
        Collection<CuratorTransactionResult> commit = curatorFramework2.inTransaction().create().forPath("/curator3-3", "风门坳村".getBytes())
                .and().setData().forPath("/curator2-2", "你随便".getBytes()).and().commit();
        commit.forEach(vo -> {
            System.out.println(vo.getType() + "->" + vo.getResultPath());
        });

    }
}
