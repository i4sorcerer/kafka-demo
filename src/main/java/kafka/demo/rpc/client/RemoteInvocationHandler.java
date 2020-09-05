package kafka.demo.rpc.client;

import kafka.demo.rpc.server.RpcRequest;
import kafka.demo.rpc.zk.IServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : sk
 */
@Slf4j
public class RemoteInvocationHandler implements InvocationHandler {
    private String host;
    private int port;
    private IServiceDiscovery discovery =null;
    private String version;
    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 注册中心方式
     * @param discovery
     * @param version
     */
    public RemoteInvocationHandler(IServiceDiscovery discovery, String version) {
        this.discovery=discovery;
        this.version=version;
    }
    public RemoteInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //组装请求
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setVersion(version);
        // 注册中心方式
        return invokeWithRegisty(request);

        // 直接调用方式
//        return invokeDirect(request);
    }

    /**
     * 直接通过host和port进行调用
     *
     * @return
     */
    private Object invokeDirect(RpcRequest request) {
        //通过tcp传输协议进行传输
        TCPTransport tcpTransport = new TCPTransport(host + ":" + port);
        //发送请求
        return tcpTransport.send(request);
    }

    /**
     * 通过zk的注册中心实现调用
     *
     * @return
     */
    private Object invokeWithRegisty(RpcRequest request) {
        //根据接口名称和版本号得到对应的服务地址
        String serviceAddress=discovery.discovery(request.getClassName()+"-"+this.version);
        //通过tcp传输协议进行传输
        TCPTransport tcpTransport = new TCPTransport(serviceAddress);
        //发送请求
        return tcpTransport.send(request);
    }

}
