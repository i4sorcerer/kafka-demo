package kafka.demo.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 使用curator方式，实现分布式锁
 *
 * @author : sk
 */
public class DistributeLockCurator {
    public static void main(String[] args) {
        CuratorFramework client = CuratorFrameworkFactory.newClient("",new ExponentialBackoffRetry(1000,3));
        InterProcessMutex lock = new InterProcessMutex(client,"/locks");
        try {
            // 获取锁
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
