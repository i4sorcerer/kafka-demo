package kafka.demo.rpc.zk;

/**
 * 注册中心接口类
 */
public interface IRegisterCenter {
    /**
     * 向zk注册服务名称和服务地址
     * @param serviceName
     * @param address
     */
    void register(String serviceName,String address);
}
