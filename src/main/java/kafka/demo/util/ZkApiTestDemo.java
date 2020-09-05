package kafka.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper的api客户端使用demo
 *
 * @author : sk
 */
@Slf4j
public class ZkApiTestDemo implements Watcher {
    private final CountDownLatch count = new CountDownLatch(1);
    public ZooKeeper zk = null;

    public ZkApiTestDemo() {
        try {
            this.zk = new ZooKeeper("192.168.146.130:2181,192.168.146.130:2182,192.168.146.130:2183", 5000, this);
            count.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ZkApiTestDemo zkDemo = new ZkApiTestDemo();
        log.info(zkDemo.zk.getState().toString());

        // 创建节点
        try {
            String path = "/spring";
            Stat stEst1 = zkDemo.zk.exists(path, true);
            if (stEst1 == null) {
                String rs = zkDemo.zk.create(path, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info(path + "znode created result :" + rs);
                Thread.sleep(1000);
            }

            // get当前节点state
            Stat st = new Stat();
            String getRs = new String(zkDemo.zk.getData(path, null, st));
            log.info(path + " znode getData rs:" + getRs);
            log.info(path + " znode pth stat :" + st);

            // 修改节点前先要get节点
            st = zkDemo.zk.setData(path, "1".getBytes(), st.getVersion());
            log.info(path + " znode pth stat :" + st);

            // 删除节点
//            zkDemo.zk.delete(path, st.getVersion());
            // watcher 事件机制
            // 通过exists来注册事件，
            Stat stEst = zkDemo.zk.exists(path, true);
            log.info(path + " znode exists : " + stEst);
            System.in.read();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // notconnected
        // connecting
        // connectted
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info("全局的默认watcher process->" + watchedEvent.getType());
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            count.countDown();
        }
        // 循环注册exists事件
        try {
            this.zk.exists("/spring", true);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
