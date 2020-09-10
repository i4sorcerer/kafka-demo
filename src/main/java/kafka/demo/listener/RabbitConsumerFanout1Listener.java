package kafka.demo.listener;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbitmq消费端监听器 Fanout
 *
 * @author : sk
 */
@Component
@Slf4j
@RabbitListener(queues = "TEST-FANOUT-QUEUE1")
public class RabbitConsumerFanout1Listener {
    /**
     * 消息处理方法
     *
     * @param testMsg
     */
    @RabbitHandler
    public void processMsg(Map testMsg){
        log.info("接收到 " + RabbitConstant.TEST_FANOUT_QUEUE1+" 消息->" +testMsg);
    }

}
