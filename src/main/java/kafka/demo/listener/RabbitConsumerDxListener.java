package kafka.demo.listener;

import com.rabbitmq.client.Channel;
import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbitmq消费端监听器 dx
 *
 * @author : sk
 */
@Component
@Slf4j
//@RabbitListener(containerFactory = "rabbitListenerContainerFactory",
//        bindings = @QueueBinding(value = @Queue("TEST-DX-QUEUE"),
//                exchange = @Exchange(value = "TEST-DIRECTEXCHANGE", type = ExchangeTypes.DIRECT), key = "test.direct"))
//@RabbitListener(queues = "TEST-DX-QUEUE")
public class RabbitConsumerDxListener {
    /**
     * 消息处理方法
     *
     * @param testMsg
     */
//    @RabbitHandler
    public void processMsg(Map testMsg) {
//        log.info("接收到 " + RabbitConstant.TEST_DX_QUEUE + " headers消息->" + headers);
        log.info("接收到 " + RabbitConstant.TEST_DX_QUEUE + " payload消息->" + testMsg);
    }

}
