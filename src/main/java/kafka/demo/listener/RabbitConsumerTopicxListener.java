package kafka.demo.listener;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbitmq消费端监听器 Topic
 *
 * @author : sk
 */
@Component
@Slf4j
@RabbitListener(queues = "TEST-TOPIC-QUEUE")
public class RabbitConsumerTopicxListener {
    /**
     * 消息处理方法
     *
     * @param testMsg
     */
    @RabbitHandler
    public void processMsg(Map testMsg){
        log.info("接收到 " + RabbitConstant.TEST_TOPIC_QUEUE+" 消息->" +testMsg);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
