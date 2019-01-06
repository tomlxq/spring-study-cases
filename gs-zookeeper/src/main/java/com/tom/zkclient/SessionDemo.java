package com.tom.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SessionDemo {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient(CONNECTIONS, 5000);
        System.out.println(zkClient + " -> success");
        //创建节点
        if (!zkClient.exists("/zkClient")) {
            zkClient.createEphemeral("/zkClient");
        }
        System.out.println("创建节点成功");
        zkClient.createPersistent("/zkClient1/zkClient1-1/zkClient1-1-1", true);
        zkClient.createPersistent("/zkClient1/zkClient1-1/zkClient1-1-2", true);
        zkClient.createPersistent("/zkClient1/zkClient1-2/zkClient1-2-1", true);
        zkClient.createPersistent("/zkClient1/zkClient1-2/zkClient1-2-2", true);
        System.out.println("创建多个子节点成功");
        List<String> children = zkClient.getChildren("/zkClient1");
        System.out.println("获取子节点->" + children.toString());
        //订阅事件
        zkClient.subscribeDataChanges("/zkClient1", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) {
                System.out.println("操作数据变化　节点名称: " + s + " -> 修改后节点的值: " + o);
            }

            @Override
            public void handleDataDeleted(String s) {
            }
        });
        zkClient.subscribeChildChanges("/zkClient1", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) {
                System.out.println("操作子节点变化　节点名称: " + s + " -> 修改后子节点的名称: " + list.toString());
            }
        });
        zkClient.writeData("/zkClient1", "123456");
        TimeUnit.SECONDS.sleep(2);
        zkClient.createPersistent("/zkClient1/zkClient1-3/zkClient1-3-1", true);
        TimeUnit.SECONDS.sleep(2);
        zkClient.deleteRecursive("/zkClient1");
        System.out.println("循环删除非空节点成功");

        zkClient.close();
    }
}
