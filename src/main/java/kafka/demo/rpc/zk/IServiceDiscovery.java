package kafka.demo.rpc.zk;

/**
 * 服务发现接口
 */
public interface IServiceDiscovery {
    /**
     * 根据请求服务名称，获取其调用地址
     *
     * @param serviceName
     * @return
     */
    String discovery(String serviceName);
}
