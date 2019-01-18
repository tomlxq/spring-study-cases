package com.tom.master;


import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MasterSelector {
    /**
     * 需要争抢的master节点
     */
    public static final String MASTER_PATH = "/master";
    /**
     * 是否在运行中
     */
    private static boolean isRunning = false;
    ZkClient zkClient;//
    /**
     * 注册节点的变化
     */
    IZkDataListener zkDataListener;
    /**
     * master服务器
     */
    UserCenter master;
    /**
     * 其它服务器
     */
    UserCenter slave;
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public MasterSelector(UserCenter slave, ZkClient zkClient) {
        System.out.println(slave + "->去争抢master");
        this.zkClient = zkClient;
        this.slave = slave;
        this.zkDataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) {

            }

            @Override
            public void handleDataDeleted(String s) {
                System.out.println(s + "->发生删除事件，重新选举master");
                chooseMaster();
            }
        };
    }

    /**
     * 开始选举
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            zkClient.subscribeDataChanges(MASTER_PATH, zkDataListener);//注册节点事件
            chooseMaster();
        }
    }

    /**
     * 停止选举
     */
    public void stop() {
        if (isRunning) {
            isRunning = false;
            zkClient.unsubscribeDataChanges(MASTER_PATH, zkDataListener);//注册节点事件
            releaseMaster();
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 实现选举的逻辑过程
     */
    private void chooseMaster() {
        if (!isRunning) {
            System.out.println("当前服务没有启动！");
            return;
        }
  /*      System.out.println("选举前的slave"+this.slave);
        UserCenter userCenter1 = zkClient.readData(MASTER_PATH, true);
        System.out.println("选举前的userCenter"+userCenter1);*/
        try {
            zkClient.createEphemeral(MASTER_PATH, this.slave);
            this.master = this.slave;
            System.out.println(this.master.getName() + " 我已经是master,你们要听我的");
            //定时器 master释放 表示master出现故障 释放master
            scheduledExecutorService.schedule(() -> {
                releaseMaster();
            }, 5, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            //表示master已经存在
            UserCenter userCenter = zkClient.readData(MASTER_PATH, true);
            if (null == userCenter) {
                chooseMaster();
            } else {
                master = userCenter;
            }
        }
    }


    /**
     * 实放master
     */
    private void releaseMaster() {
        //释放master,故障模拟过程
        //判断当前是不是master,如果是master才需要释放
        if (checkIsMaster()) {
            zkClient.deleteRecursive(MASTER_PATH);
        }

    }

    private boolean checkIsMaster() {
        UserCenter userCenter = zkClient.readData(MASTER_PATH);
        return StringUtils.equals(userCenter.getName(), slave.getName());
    }
}
