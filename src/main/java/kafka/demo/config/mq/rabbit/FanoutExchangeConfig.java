package kafka.demo.config.mq.rabbit;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FanoutoutExchange配置类
 * @author : sk
 */
@Configuration
@Slf4j
public class FanoutExchangeConfig {
    /**
     * 生成queue的bean
     * @return
     */
    @Bean
    public Queue testFanoutQueue1(){
        return new Queue(RabbitConstant.TEST_FANOUT_QUEUE1,true);
    }
    @Bean
    public Queue testFanoutQueue2(){
        return new Queue(RabbitConstant.TEST_FANOUT_QUEUE2,true);
    }

    /**
     * 生成FanoutExchange
     * @return
     */
    @Bean
    public FanoutExchange testFanoutExchange(){
        return new FanoutExchange(RabbitConstant.TEST_FANOUT_EXCHANGE,true,false);
    }

    /**
     * 绑定实例生成
     * @return
     */
    @Bean
    public Binding bindFanout1(Queue testFanoutQueue1,FanoutExchange testFanoutExchange){
        return  BindingBuilder.bind(testFanoutQueue1).to(testFanoutExchange);
    }
    @Bean
    public Binding bindFanout2(Queue testFanoutQueue2,FanoutExchange testFanoutExchange){
        return  BindingBuilder.bind(testFanoutQueue2).to(testFanoutExchange);
    }

}
