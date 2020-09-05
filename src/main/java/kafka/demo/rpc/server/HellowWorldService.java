package kafka.demo.rpc.server;

import kafka.demo.rpc.zk.annotation.RpcAnnotation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : sk
 */
@Slf4j
@RpcAnnotation(name = IService.class)
public class HellowWorldService implements IService {
    @Override
    public String service(String arg) {
        return "hello world service ->" + arg;
    }
}
