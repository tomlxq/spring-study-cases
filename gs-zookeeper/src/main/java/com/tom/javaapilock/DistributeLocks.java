package com.tom.javaapilock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class DistributeLocks {
    /**
     * 分布式锁有个根节点
     */
    public static final String ROOT_LOCK = "/LOCKS";
    private final static byte[] data = {1, 2};
    /**
     * 获取会话连结
     */
    private ZooKeeper zooKeeper;
    /**
     * 会话超时时间
     */
    private int sessionTimeout;
    /**
     * 记录锁节点名称
     */
    private String lockId;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public DistributeLocks() throws IOException, InterruptedException {
        //create /LOCKS 0,0
        this.zooKeeper = ClientUtils.getInstance();
        this.sessionTimeout = ClientUtils.getSessionTimeout();
    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                DistributeLocks distributeLocks = null;
                try {
                    distributeLocks = new DistributeLocks();
                    countDownLatch.countDown();
                    countDownLatch.await();
                    distributeLocks.lock();
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (null != distributeLocks) {
                        distributeLocks.unLock();
                    }
                }
            }).start();
        }
    }

    /**
     * 获取锁
     *
     * @return
     */
    public boolean lock() {
        try {
            /**
             * ROOT_LOCK+"/"  如果没有名称，后面会自动序号  /LOCKS//0000000001
             */
            lockId = zooKeeper.create(ROOT_LOCK + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + " 成功的创建了LOCK节点【" + lockId + "】，开始去竞争锁->");
            /**
             * 获取根节点下的所有节点
             */
            List<String> children = zooKeeper.getChildren(ROOT_LOCK, true);
            SortedSet<String> sortedSet = new TreeSet<>();
            children.forEach(vo -> {
                sortedSet.add(ROOT_LOCK + "/" + vo);
            });
            String first = sortedSet.first();
            if (first.equals(lockId)) {
                System.out.println(Thread.currentThread().getName() + " 成功的获取了锁->" + lockId);
                return true;
            }
            SortedSet<String> lessThanLockId = sortedSet.headSet(lockId);
            if (!lessThanLockId.isEmpty()) {
                //拿到比当前节点更小的上一个节点
                String preLockId = lessThanLockId.last();
                zooKeeper.exists(preLockId, new LockWatcher(countDownLatch));
                //意味着会话超时或节点被删除释放了
                countDownLatch.await(sessionTimeout, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + " 成功的获取了锁->" + lockId);
            }
            return true;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @return
     */
    public boolean unLock() {
        System.out.println(Thread.currentThread().getName() + " 开始释放锁->" + lockId);
        try {
            zooKeeper.delete(lockId, -1);
            System.out.println(Thread.currentThread().getName() + "节点【" + lockId + "】被成功删除");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }
}
