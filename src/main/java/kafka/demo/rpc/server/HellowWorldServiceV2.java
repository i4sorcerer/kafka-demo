package kafka.demo.rpc.server;

import kafka.demo.rpc.zk.annotation.RpcAnnotation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : sk
 */
@Slf4j
@RpcAnnotation(name = IService.class,version = "2.0")
public class HellowWorldServiceV2 implements IService {
    @Override
    public String service(String arg) {
        return "hello world service v2.0 ->" + arg;
    }
}
