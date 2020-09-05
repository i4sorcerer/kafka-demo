package kafka.demo.rpc.balance;


import java.util.List;

/**
 * /**
 * 服务负载均衡
 * @author : sk
 */
public interface IBalanceService {
    /**
     * 负载均衡方法
     * @param lists
     * @return
     */
    String loadBalance(List<String> lists);
}
