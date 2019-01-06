package com.tom.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorUtils {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";

    public static CuratorFramework getInstance() {
        CuratorFramework curatorFramework1 =
                CuratorFrameworkFactory.newClient(CONNECTIONS, 5000, 2000,
                        new ExponentialBackoffRetry(1000, 3));
        curatorFramework1.start();
        return curatorFramework1;
    }
}
