package kafka.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author : sk
 */
@Slf4j
public class ZkTestDemo2 {
    private static ZooKeeper zk;
    private static final String PATH = "/spring";

    public static void main(String[] args) throws Exception {
        CountDownLatch count = new CountDownLatch(1);
        zk = new ZooKeeper("192.168.146.130:2181,192.168.146.130:2182,192.168.146.130:2183", 5000, watcher -> {
            if (watcher.getState() == Watcher.Event.KeeperState.SyncConnected) {
                log.info("server connected.");
                count.countDown();
            }
            log.info(PATH + " zode wateevent " + watcher.getType());
            //设置循环监听
            if (watcher.getType() == Watcher.Event.EventType.NodeDataChanged) {
                // 设置循环监听
                try {
                    zk.exists(PATH, true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 等待zk连接
        count.await();

        // getData
        Stat orgStat = new Stat();
        zk.getData(PATH, null, orgStat);
        // 通过exists方法注册一此watcher
        zk.exists(PATH, true);
        // 触发NodeDataChanged事件
        zk.setData(PATH, "1".getBytes(), orgStat.getVersion());
        System.in.read();
    }
}
