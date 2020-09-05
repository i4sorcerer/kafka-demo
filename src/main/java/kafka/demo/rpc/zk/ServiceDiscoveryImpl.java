package kafka.demo.rpc.zk;

import kafka.demo.rpc.balance.IBalanceService;
import kafka.demo.rpc.balance.RandomLoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import java.util.List;

/**
 * 实现服务的发现
 * 1. 服务发现
 * 2. 服务负载均衡
 * 3. 服务上下线的动态感知（地址缓存在客户端）
 *
 * @author : sk
 */
@Slf4j
public class ServiceDiscoveryImpl implements IServiceDiscovery {
    private CuratorHelp curator =null;
    private List<String> serverAddressCache;

    private IBalanceService balanceService=null;
    public ServiceDiscoveryImpl() {
        // 初始化curator，新建立一个客户端连接
        this.curator=new CuratorHelp();
        // 默认随机负载均衡策略
        this.balanceService=new RandomLoadBalance();
    }

    @Override
    public String discovery(String serviceName) {
        // 查询当前服务名称节点下的所有子节点（地址list）
        String servicePath = ZkConfig.ROOT_NODE+"/"+serviceName;
        // 获取当前服务的所有地址节点
        serverAddressCache = curator.getChildrenNodes(servicePath);
        log.info(String.format("注册中心发现所有在线地址->%s,服务名称->%s",serverAddressCache,serviceName));
        // 注册watcher事件，监控当前节点的所有子节点的上下线
        registerAddressWatcher(servicePath);
        // 根据一定规则选取一个地址访问
        String address = balanceService.loadBalance(serverAddressCache);
        log.info(String.format("注册中心返回访问地址->%s,服务名称->%s",address,serviceName));
        // 返回地址
        return address;
    }

    /**
     * 注册watcher事件通知，随时更新节点地址缓存
     *
     * @param servicePath
     */
    private void registerAddressWatcher(String servicePath) {
        CuratorCache cache = CuratorCache.builder(curator.getClient(),servicePath).build();
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forPathChildrenCache(servicePath,curator.getClient(),(curatorFramework,pathChildrenCacheEvent)->{
                    // 获得子节点删除，增加通知事件
                    log.info("子节点变更通知->"+pathChildrenCacheEvent.getType());
                    // 通知事件收到后，再一次获取当前节点的所欲子节点，并更新本地缓存
                    serverAddressCache=curatorFramework.getChildren().forPath(servicePath);
                }).build();
        cache.listenable().addListener(listener);
        cache.start();
    }
}
