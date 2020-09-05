package kafka.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 利用zookeeper原生api实现分布式锁
 *
 * @author : sk
 */
@Slf4j
public class DistributeLockZk implements Lock, Watcher {
    private ZooKeeper zk = null;
    private String rootNode = "/locks";
    private String currentLock = null;
    private String waitLock = null;

    private static final String SERVER_STRING = "192.168.146.130:2181,192.168.146.130:2182,192.168.146.130:2183";

    private CountDownLatch countDown = null;

    public DistributeLockZk() throws IOException {
        zk = new ZooKeeper(SERVER_STRING, 5000, this);

    }

    @Override
    public void lock() {
        if (tryLock()) {
            log.info(String.format("线程 %s 获得锁成功->%s", Thread.currentThread(), currentLock));
        }
        waitForLock();
    }

    private boolean waitForLock() {
        // 对当前的waiLock节点添加监视器
        Stat waiSt = null;
        try {
            waiSt = zk.exists(waitLock, true);
            if (waiSt != null) {
                countDown = new CountDownLatch(1);
                // 节点的锁状态并没有释放，阻塞等待节点释放
                log.info(String.format("%s 线程正在等待%s的释放", Thread.currentThread(), waitLock));
                countDown.await();
                log.info(String.format("线程 %s 获得锁成功->%s", Thread.currentThread(), currentLock));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            Stat st = zk.exists(rootNode, false);
            if (st == null) {
                // 根节点不存在创建
                zk.create(rootNode, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // 注册子节点,有序临时节点
            currentLock = zk.create(rootNode + "/", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zk.getChildren(rootNode, false);
            SortedSet set = new TreeSet();
            for (String child : children) {
                set.add(rootNode + "/" + child);
            }

            // first node
            String first = (String) set.first();
            // 如果是最小的节点，则此节点获得锁

            if (first.equals(currentLock)) {
                // 获得锁
                return true;
            }
            SortedSet preSet = set.headSet(currentLock);
            if (!preSet.isEmpty()) {
                // 小于此节点值得子集存在的时候，返回小于当前节点的最大的一个节点
                waitLock = (String) preSet.last();
            }
            // 锁节点已经被释放，触发下一个节点的锁的获取
            return false;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            // 强制释放锁，删除锁节点
            zk.delete(rootNode + "/" + currentLock, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
//        if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitLock)) {
        if (countDown != null) {
            try {
                log.info(String.format("目前所有子节点->%s", zk.getChildren(rootNode, false)));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 当节点被删除时，调用coutdown，唤醒下一个节点获取锁
            countDown.countDown();
        }
    }

    public static void deleteAllChild(String path) {
        try {
            ZooKeeper zk = new ZooKeeper(SERVER_STRING, 5000, null);
            // 删除所有子节点
            List<String> children = zk.getChildren("/locks", false);
            for (String child : children) {
                zk.delete(path + "/" + child, -1);
                log.info(String.format("%s node deletd ", path + "/" + child));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        deleteAllChild("/locks");
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DistributeLockZk dsl = new DistributeLockZk();
                        dsl.lock();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
