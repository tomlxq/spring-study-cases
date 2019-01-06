package com.tom.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

/**
 * 三种watcher来做节点的监听
 * NodeCache 监视一个节点的创建，更新，删除
 * PathChildrenCache  监视一个路径下子节点的创建，删除，节点数据的更新
 * TreeCache NodeCache+PathChildrenCache的合体，监视路径下的创建，更新，删除事件
 */
public class CuratorEventDemo {
    public static void main(String[] args) throws Exception {
        CuratorFramework instance = CuratorUtils.getInstance();
        NodeCache nodeCache = new NodeCache(instance, "/CuratorEvent", false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(() -> System.out.println("节点数据发生变化，变化后的数据结果->" + new String(nodeCache.getCurrentData().getData())));
        instance.delete().deletingChildrenIfNeeded().forPath("/CuratorEvent");
        TimeUnit.SECONDS.sleep(1);
        instance.create().creatingParentContainersIfNeeded().forPath("/CuratorEvent", "张飞".getBytes());
        TimeUnit.SECONDS.sleep(1);
        instance.setData().forPath("/CuratorEvent", "刘备".getBytes());
        TimeUnit.SECONDS.sleep(1);

        //子节点事件
        PathChildrenCache pathChildrenCache = new PathChildrenCache(instance, "/CuratorEvent1", true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        pathChildrenCache.getListenable().addListener((CuratorFramework curatorFramework, PathChildrenCacheEvent event) -> {

            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("子节点增加->" + event.getType() + " 节点: " + event.getData().getPath());
                    break;
                case CHILD_REMOVED:
                    System.out.println("子节点删除->" + event.getType() + " 节点: " + event.getData().getPath());
                    break;
                case CHILD_UPDATED:
                    System.out.println("子节点更新->" + event.getType() + " 节点: " + event.getData().getPath());
                    break;
                default:
                    System.out.println("其它操作->" + event.getType() + " 节点: " + event.getData().getPath());
                    break;
            }
        });
        instance.delete().deletingChildrenIfNeeded().forPath("/CuratorEvent1");
        instance.create().withMode(CreateMode.PERSISTENT).forPath("/CuratorEvent1", "关云长".getBytes());
        System.out.println("执行完成1");
        TimeUnit.SECONDS.sleep(1);
        instance.create().withMode(CreateMode.PERSISTENT).forPath("/CuratorEvent1/CuratorEvent1-1", "诸葛亮".getBytes());
        System.out.println("执行完成2");
        TimeUnit.SECONDS.sleep(1);
        instance.setData().forPath("/CuratorEvent1/CuratorEvent1-1", "貂蝉".getBytes());
        System.out.println("执行完成3");
        TimeUnit.SECONDS.sleep(1);
        instance.delete().forPath("/CuratorEvent1/CuratorEvent1-1");
        System.out.println("执行完成4");
        System.in.read();


    }
}
