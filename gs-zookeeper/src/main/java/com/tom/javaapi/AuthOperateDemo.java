package com.tom.javaapi;

import com.google.common.collect.Lists;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 */
public class AuthOperateDemo {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";
    public static final Stat STAT = new Stat();
    private static CountDownLatch countDownLatch1 = new CountDownLatch(1);
    private static CountDownLatch countDownLatch2 = new CountDownLatch(1);
    private static ZooKeeper zooKeeper1;
    private static ZooKeeper zooKeeper2;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper1 = new ZooKeeper(CONNECTIONS, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch1.countDown();
                }
            }
        });
        countDownLatch1.await();
        System.out.println(zooKeeper1.getState());
        zooKeeper1.addAuthInfo("digest", "root:root".getBytes());
        ACL acl = new ACL(ZooDefs.Perms.CREATE, new Id("digest", "root:root"));
        ACL acl1 = new ACL(ZooDefs.Perms.CREATE, new Id("ip", "192.168.238.100"));
        List<ACL> list = Lists.newArrayList();
        list.add(acl);
        list.add(acl1);
        if (null == zooKeeper1.exists("/auth2", false)) {
            zooKeeper1.create("/auth2", "123".getBytes(), list, CreateMode.EPHEMERAL);
        }
        //创建节点
        if (null == zooKeeper1.exists("/auth1", false)) {
            String ret = zooKeeper1.create("/auth1", "123".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
            System.out.println("创建成功：" + ret);
        }
        zooKeeper2 = new ZooKeeper(CONNECTIONS, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch2.countDown();
                }
            }
        });
        countDownLatch2.await();
        //zooKeeper2.addAuthInfo("digest","root:root".getBytes());
        zooKeeper2.delete("/auth1", -1);
        zooKeeper2.delete("/auth2", -1);
    }


}
