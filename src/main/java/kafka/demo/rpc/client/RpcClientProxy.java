package kafka.demo.rpc.client;

import kafka.demo.rpc.zk.IServiceDiscovery;
import kafka.demo.rpc.zk.ServiceDiscoveryImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * @author : sk
 */
@Slf4j
public class RpcClientProxy {
    private IServiceDiscovery discovery =null;

    public RpcClientProxy(IServiceDiscovery discovery) {
        this.discovery=discovery;
    }

    /**
     * 通过服务发现：创建客户端的远程代理。通过远程代理进行访问
     *
     * @param interfaceCls
     * @param <T>
     * @return
     */
    public <T> T clientProxy(final Class<T> interfaceCls, String version) {
        //使用到了动态代理。
        log.info("使用注册中心，客户端代理被调用");
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class[]{interfaceCls}, new RemoteInvocationHandler(discovery,version));
    }

    /**
     * 创建客户端的远程代理。通过远程代理进行访问
     *
     * @param interfaceCls
     * @param <T>
     * @return
     */
    public <T> T clientProxy(final Class<T> interfaceCls, String host, int port) {
        //使用到了动态代理。
        log.info("客户端代理被调用");
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class[]{interfaceCls}, new RemoteInvocationHandler(host, port));
    }

}
