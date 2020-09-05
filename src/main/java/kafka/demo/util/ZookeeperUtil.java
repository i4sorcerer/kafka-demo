package kafka.demo.util;

import com.google.common.io.Closeables;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * zk操作共同类（通过curator客户端实现）
 *
 * @author : sk
 */
@Slf4j
public class ZookeeperUtil {
    String forPath = "/sk";
    private final CuratorFramework curator;
    private static final String CONNECT_STR = "192.168.146.130:2181,192.168.146.130:2182,192.168.146.130:2183";

    public ZookeeperUtil() throws Exception {
//        curator = CuratorFrameworkFactory.builder().connectString(CONNECT_STR)
//                .sessionTimeoutMs(150000)
//                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
//                .namespace("curator").build();
        curator = CuratorFrameworkFactory.newClient(CONNECT_STR,
                new ExponentialBackoffRetry(1000, 3));
        // 启动连接
        curator.start();
        // 启动cache监听
        //adddListenerWithCuratorCache(curator, forPath);
        log.info("zkutil启动成功！");
    }

    public CuratorFramework getCurator() {
        return this.curator;
    }

    public static void main(String[] args) throws Exception {
        ZookeeperUtil zk = new ZookeeperUtil();
        CuratorFramework curator = zk.getCurator();
        boolean nodeExist = false;
        Stat stC = new Stat();
        try {
            String rs = new String(zk.getCurator().getData().storingStatIn(stC).forPath(zk.forPath));
            log.info(stC.toString());
            log.info("节点是否存在:" + rs);
            nodeExist = StringUtils.isEmpty(rs) ? false : true;
        } catch (Exception e) {
            log.info("node " + zk.forPath + " not exists.");
            nodeExist = false;
        }
        // 创建节点
        if (!nodeExist) {
            log.info(" to create znode " + zk.forPath);
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zk.forPath);
        }
        // 获取节点
        curator.getData().storingStatIn(stC).forPath(zk.forPath);
        zk.adddListenerWithCuratorCache(curator, zk.forPath);
        // 修改节点
        curator.setData().withVersion(stC.getVersion()).forPath(zk.forPath, "sksksk1".getBytes());
        curator.setData().forPath(zk.forPath,"sksksk2".getBytes());
        curator.setData().forPath(zk.forPath,"sksksk3".getBytes());

        curator.close();
    }

    private void addListenerWthTreeCache() {
        // treeCache最新版本中已经被deprecated
    }

    /**
     * 通过CuratorCache设置事件监听器
     *
     * @param curator
     * @param forPath
     * @throws Exception
     */
    private void adddListenerWithCuratorCache(CuratorFramework curator, String forPath) throws Exception {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try (CuratorCache cache = CuratorCache.build(curator, forPath)) {
            CuratorCacheListener curatorCacheListener = CuratorCacheListener.builder()
                    .forCreates(node -> log.info("created event znode " + node.getPath()))
                    .forChanges((oldNode, node) -> log.info(String.format("node changed oldNode:%s node:%s", oldNode, node)))
                    .forDeletes(delNode -> log.info(String.format("node deleted: %s", delNode)))
                    .forInitialized(() -> log.info(String.format("cache initialized %s", Thread.currentThread())))
                    .build();
            cache.listenable().addListener(curatorCacheListener);
            cache.start();
            log.info("curator cache started.");
            System.in.read();
        }
    }

}
