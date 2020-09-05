package kafka.demo.rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author : sk
 */
public class CuratorHelp {
    private CuratorFramework client = null;

    public CuratorFramework getClient(){
        return this.client;
    }
    public CuratorHelp() {
        this.client = CuratorFrameworkFactory.newClient(ZkConfig.SERVER_STR,
                new ExponentialBackoffRetry(1000, 3));
        // 启动客户端
        this.client.start();
    }

    /**
     * 节点存在与否的校验
     *
     * @param nodeName
     * @return
     */
    public boolean checkNodeExists(String nodeName){
        try {
            Stat st = client.checkExists().forPath(nodeName);
            if (st==null){
                // 节点不存在
                return false;
            }
            // 节点存在
            return true;
        } catch (Exception e) {
            throw new RuntimeException("节点存在验证失败",e);
        }
    }
    public String registerServiceAddress(String serviceName,String address){
        // 判断服务节点存在不存在则创建，持久化节点
        String serviceFullPath = ZkConfig.ROOT_NODE+"/"+serviceName;
        if (!checkNodeExists(serviceFullPath)){
            // 服务节点不存在，则创建。根节点不存在也自动创建
            try {
                String node=client.create().creatingParentsIfNeeded().forPath(serviceFullPath);
            } catch (Exception e) {
                throw new RuntimeException("注册服务节点报错",e);
            }
        }
        // 在持久化节点下，创建子节点。注册服务地址
        String addressPath = serviceFullPath+"/"+address;
        String node=null;
        try {
            // 创建临时节点，存储地址
            node=client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
        } catch (Exception e) {
            throw new RuntimeException("注册地址节点报错",e);
        }
        return node;
    }

    /**
     * 获取当前节点的所有子节点
     *
     * @param nodePath
     * @return
     */
    public List<String> getChildrenNodes(String nodePath){
        try {
            List<String> children= client.getChildren().forPath(nodePath);
            return children;
        } catch (Exception e) {
            throw new RuntimeException("获取节点的所有子节点失败->"+nodePath,e);
        }
    }
}
