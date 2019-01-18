package com.tom.master;

import com.google.common.collect.Lists;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasterDemo {
    public static final String CONNECTIONS = "192.168.238.105:2181,192.168.238.110:2181," +
            "192.168.238.115:2181,192.168.238.120:2181";

    public static void main(String[] args) throws InterruptedException {
        /**
         * 保存所有的zkClients列表
         */
        List<MasterSelector> selectorLists = Lists.newArrayList();
        List<ZkClient> clientLists = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            ZkClient zkClient = new ZkClient(CONNECTIONS, 5000, 5000, new SerializableSerializer());
            UserCenter userCenter = new UserCenter(i, "客户端：" + i);
            MasterSelector masterSelector = new MasterSelector(userCenter, zkClient);
            masterSelector.start();
            selectorLists.add(masterSelector);
            TimeUnit.SECONDS.sleep(4);
        }
        selectorLists.forEach(vo -> {
            vo.stop();
        });
    }
}
