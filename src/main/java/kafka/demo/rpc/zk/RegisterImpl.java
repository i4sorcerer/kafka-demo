package kafka.demo.rpc.zk;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : sk
 */
@Slf4j
public class RegisterImpl implements IRegisterCenter {
    private CuratorHelp curator=null;
    public RegisterImpl() {
        this.curator=new CuratorHelp();
    }

    /**
     * 注册服务
     * @param serviceName
     * @param address
     */
    @Override
    public void register(String serviceName, String address) {
        String addressNode =curator.registerServiceAddress(serviceName,address);
        log.info(String.format("成功注册节点->"+addressNode));
    }
}
