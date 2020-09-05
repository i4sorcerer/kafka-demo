package kafka.demo.rpc.balance;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * 随机访问的负载均衡策略
 *
 * @author : sk
 */
public class RandomLoadBalance implements IBalanceService {
    @Override
    public String loadBalance(List<String> lists) {
        if (CollectionUtils.isEmpty(lists))
            return null;
        if (lists.size()==1){
            // 如果只有一个地址，直接返回
            return lists.get(0);
        }
        // 否则进行负载均衡
        Random random = new Random();
        return lists.get(random.nextInt(lists.size()));
    }
}
