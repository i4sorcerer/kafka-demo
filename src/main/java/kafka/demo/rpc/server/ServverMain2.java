package kafka.demo.rpc.server;

import kafka.demo.rpc.zk.IRegisterCenter;
import kafka.demo.rpc.zk.RegisterImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author : sk
 */
@Slf4j
public class ServverMain2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // 实际的服务类
        IService service = new HellowWorldService();
        IService service2 = new HellowWorldServiceV2();
        IRegisterCenter registerCenter = new RegisterImpl();
        RpcServer rcpServer = new RpcServer(registerCenter, "127.0.0.1:8080");
        // 绑定服务和地址
        rcpServer.bind(service, service2);
        // 发布注册服务，监听
        rcpServer.publish();
        System.in.read();

    }
}
