package com.tom.javaapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App implements Watcher {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";
    public static final Stat STAT = new Stat();
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(CONNECTIONS, 5000, new App());
        countDownLatch.await();
        System.out.println(zooKeeper.getState());
        //创建节点
        String ret = zooKeeper.create("/tom", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建成功：" + ret);
        zooKeeper.getData("/tom", true, STAT);
        Thread.sleep(2000);
        //修改数据
        Stat stat1 = zooKeeper.setData("/tom", "456".getBytes(), -1);
        zooKeeper.exists("/tom", true);
        System.out.println("修改修改数据：" + stat1.toString());
        Thread.sleep(2000);
        Stat stat2 = zooKeeper.setData("/tom", "789".getBytes(), -1);
        zooKeeper.exists("/tom", true);
        System.out.println("修改修改数据：" + stat2.toString());
        zooKeeper.delete("/tom", -1);
        Thread.sleep(2000);
        zooKeeper.create("/tom1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.exists("/tom1", true);
        zooKeeper.delete("/tom1", -1);
        zooKeeper.exists("/tom1", true);
        Thread.sleep(2000);
        //创建节点和子节点
        zooKeeper.create("/tom2", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.exists("/tom2", true);
        TimeUnit.SECONDS.sleep(1);
        zooKeeper.create("/tom2/tom2-1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.exists("/tom2/tom2-1", true);
        zooKeeper.create("/tom2/tom2-2", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zooKeeper.exists("/tom2/tom2-2", true);
        //获取子节点
        List<String> children = zooKeeper.getChildren("/tom2", true);
        System.out.println("获取子节点数据：" + children);
        TimeUnit.SECONDS.sleep(1);
        zooKeeper.setData("/tom2/tom2-2", "456".getBytes(), -1);
        zooKeeper.exists("/tom2/tom2-2", true);
        zooKeeper.delete("/tom2/tom2-1", -1);
        zooKeeper.delete("/tom2/tom2-2", -1);
        zooKeeper.delete("/tom2", -1);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            countDownLatch.countDown();
            try {
                if (watchedEvent.getType() == Event.EventType.None && null == watchedEvent.getPath()) {
                    System.out.println("->" + watchedEvent.getType());
                }
                if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                    System.out.println(" 节点创建后的路径：" + watchedEvent.getPath() + " 节点创建后的值：" + new String(zooKeeper.getData(watchedEvent.getPath(), false, STAT)));
                }
                if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                    System.out.println(" 节点删除后的路径：" + watchedEvent.getPath());
                }
                if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                    System.out.println(" 子节点改变后的路径：" + watchedEvent.getPath() + " 子节点改变后的值：" + new String(zooKeeper.getData(watchedEvent.getPath(), false, STAT)));
                }
                if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println(" 节点数据改变后的路径：" + watchedEvent.getPath() + " 节点数据改变后的值：" + new String(zooKeeper.getData(watchedEvent.getPath(), false, STAT)));
                }
                System.out.println(watchedEvent.getType());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
