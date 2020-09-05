package kafka.demo.rpc.zk;

/**
 * 注册中心的配置类
 */
public interface ZkConfig {
    String SERVER_STR = "192.168.146.130:2181,192.168.146.130:2182,192.168.146.130:2183";
    String ROOT_NODE = "/registry";
}

