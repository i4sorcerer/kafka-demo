package kafka.demo.config.mq.rabbit;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * DirectExchange配置类
 *
 * @author : sk
 */
@Slf4j
@Configuration
public class DirectExchangeConfig {


    @Value("${app.rabbitmq.queue.direct.deadletter}")
    private String directDetterLetterQueue;

    @Value("${app.rabbitmq.exchange.common.deadletter}")
    private String commonDetterLetterExchange;

    @Value("${app.rabbitmq.exchange.routing.key.common.deadletter}")
    private String commonDetterLetterExchangeRoutingKey;

    @Value("${app.rabbitmq.exchange.routing.key.direct.deadletter}")
    private String directDetterLetterExchangeRoutingKey;

    /**
     * 生成queue的bean
     * 1. 绑定死信交换机
     *
     * @return
     */
    @Bean
    public Queue testDxQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put(RabbitConstant.QUEUE_ARG_DEAD_LETTER_EXCHANGE, commonDetterLetterExchange);
        args.put(RabbitConstant.QUEUE_ARG_DEAD_LETTER_ROUTING_KEY, directDetterLetterExchangeRoutingKey);
        return QueueBuilder.durable("TEST-DX-QUEUE")
                .deadLetterExchange(commonDetterLetterExchange)
                .deadLetterRoutingKey(directDetterLetterExchangeRoutingKey)
                .ttl(5000)
                .build();
//        return new Queue("TEST-DX-QUEUE", true, false, false, args);
    }

    /**
     * 生成DirectExchange
     *
     * @return
     */
    @Bean
    public DirectExchange testDirectExchange() {
        return new DirectExchange(RabbitConstant.TEST_DX_EXCHANGE, true, false);
    }

    /**
     * 绑定实例生成
     *
     * @return
     */
    @Bean
    public Binding bindDirect(Queue testDxQueue, DirectExchange testDirectExchane) {
        return BindingBuilder.bind(testDxQueue).to(testDirectExchane).with(RabbitConstant.TEST_DX_ROUTING_KEY);
    }

    /**
     * 生成通用的死信队列
     *
     * @param directDetterLetterExchange
     * @return
     */
    @Bean
    public Exchange commonDeadLetterTopicExchange(@Value("${app.rabbitmq.exchange.common.deadletter}") String commonDetterLetterExchange) {
        return ExchangeBuilder.topicExchange(commonDetterLetterExchange)
                .durable(true)
                .build();
    }

    /**
     * 生成死信交换机的direct queue
     *
     * @param commonDetterLetterDirectQueue
     * @return
     */
    @Bean
    public Queue commonDeadLetterDirectQueue(@Value("${app.rabbitmq.queue.direct.deadletter}") String commonDetterLetterDirectQueue) {
        return QueueBuilder.durable(commonDetterLetterDirectQueue)
//                .deadLetterExchange(commonDetterLetterExchange)
//                .deadLetterRoutingKey(directDetterLetterExchangeRoutingKey)
                .build();
    }

    /**
     * 绑定direct queue和死信交换机
     *
     * @return
     */
    @Bean
    public Binding directDeadLetterBingding(Queue commonDeadLetterDirectQueue,TopicExchange commonDeadLetterTopicExchange){
        return BindingBuilder.bind(commonDeadLetterDirectQueue)
                .to(commonDeadLetterTopicExchange)
                .with("#");// 设置所有的死信message都可以被这个queue消费
    }
}
