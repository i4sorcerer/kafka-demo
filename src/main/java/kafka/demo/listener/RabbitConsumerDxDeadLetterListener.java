package kafka.demo.listener;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbitmq消费端监听器 dx deadletter
 *
 * @author : sk
 */
@Component
@Slf4j
//@RabbitListener(containerFactory = "rabbitListenerContainerFactory",
//        bindings = @QueueBinding(value = @Queue("TEST-DX-QUEUE"),
//                exchange = @Exchange(value = "TEST-DIRECTEXCHANGE", type = ExchangeTypes.DIRECT), key = "test.direct"))
@RabbitListener(queues = "direct-dead-letter-queue")
public class RabbitConsumerDxDeadLetterListener {
    /**
     * 消息处理方法
     *
     * @param testMsg
     */
    @RabbitHandler
    public void processMsg(Map testMsg) {
        log.info("接收到 direct-dead-letter-queue"+ " payload消息->" + testMsg);
    }

}
