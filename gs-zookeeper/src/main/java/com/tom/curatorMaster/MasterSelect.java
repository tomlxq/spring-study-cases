package com.tom.curatorMaster;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class MasterSelect {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";
    private static String CURATOR_MASTER = "/curator_master";

    public static void main(String[] args) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECTIONS)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        System.out.println(curatorFramework.getState());
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, CURATOR_MASTER, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                System.out.println("获得leader成功");
                TimeUnit.SECONDS.sleep(2);
            }

            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {

            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();//开始选举master
    }
}
