package kafka.demo.rpc.server;

import com.google.common.io.Closeables;
import kafka.demo.rpc.zk.IRegisterCenter;
import kafka.demo.rpc.zk.RegisterImpl;
import kafka.demo.rpc.zk.annotation.RpcAnnotation;
import lombok.extern.slf4j.Slf4j;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : sk
 */
@Slf4j
public class RpcServer {

    private Object service;
    private String address;
    IRegisterCenter registerCenter = null;
    private Map<String, Object> serviceCache = new HashMap<>();

    public RpcServer(IRegisterCenter registerCenter, String address) {
        this.address = address;
        // 初始化注册中心
        this.registerCenter = registerCenter;
    }

    // 线程池用来执行service
    private static final ExecutorService serverPool = Executors.newCachedThreadPool();

    /**
     * 将服务名称和服务地址绑定，并缓存到内存中
     *
     * @param services
     */
    public void bind(Object... services) {
        for (Object service : services) {
            RpcAnnotation rpcAnnotation = service.getClass().getAnnotation(RpcAnnotation.class);
            // 服务名称
            String name = rpcAnnotation.name().getName();
            // 服务版本号，不指定默认1.0
            String version = rpcAnnotation.version();
            serviceCache.put(name + "-" + version, service);
        }
    }

    /**
     * 服务发布方法
     */
    public void publish() {
        ServerSocket server = null;
        String[] addpot = address.split(":");
        try {
            server = new ServerSocket(Integer.parseInt(addpot[1]));
            // 注册中心注册服务服务绑定设置的所有服务
            for (String serviceName : serviceCache.keySet()) {
                // 将服务名称和服务地址注册到zk节点上
                registerCenter.register(serviceName, address);
            }
            log.info("服务端启动成功，服务注册中心注册完毕，监听中.....");

            while (true) {
                // 阻塞监听服务，获取一个连接
                Socket socket = server.accept();
                log.info("新建连接处理新的请求");
                // 在线程池中执行请求
//                serverPool.execute(new ProcessorHandler(this.service,socket));
                serverPool.execute(new ProcessorHandler(socket, serviceCache));
            }
        } catch (IOException e) {
            log.error("服务端获取连接失败", e);
        } finally {
            try {
                Closeables.close(server, true);
            } catch (IOException e) {
                log.error("服务端socker关闭连接失败", e);
            }
        }
    }

}
