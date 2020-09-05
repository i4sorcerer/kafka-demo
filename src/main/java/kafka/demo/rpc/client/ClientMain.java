package kafka.demo.rpc.client;

import kafka.demo.rpc.server.IService;
import kafka.demo.rpc.zk.IServiceDiscovery;
import kafka.demo.rpc.zk.ServiceDiscoveryImpl;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author : sk
 */
@Slf4j
public class ClientMain {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
//        IService service = (IService)Naming.lookup("rmi://127.0.0.1:8080/Hello");
        // Exception in thread "main" java.lang.ClassCastException:
        // sun.rmi.registry.RegistryImpl_Stub cannot be cast to kafka.demo.rpc.server.IService

        IServiceDiscovery discovery = new ServiceDiscoveryImpl();
        RpcClientProxy clientProxy = new RpcClientProxy(discovery);
        // 普通直接根据地址调用方式
//        IService service = clientProxy.clientProxy(IService.class,"127.0.0.1",8080);
        // 通过服务发现，确定服务要调用的地址
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                IService service = clientProxy.clientProxy(IService.class, "2.0");
                log.info("客户端调用结果->" + service.service("宋可使用代理调用"));
            }).start();
        }
    }

}
