package kafka.demo.config.mq.rabbit;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TopicExchange配置类
 * @author : sk
 */
@Configuration
@Slf4j
public class TopicExchangeConfig {
    @Value("${app.rabbitmq.exchange.common.deadletter}")
    private String commonDetterLetterExchange;
    /**
     * 生成queue的bean
     * @return
     */
    @Bean
    public Queue testTopicQueue(){
//        return new Queue(RabbitConstant.TEST_TOPIC_QUEUE,true);
        return QueueBuilder.durable(RabbitConstant.TEST_TOPIC_QUEUE)
                .ttl(5000)
                .deadLetterExchange(commonDetterLetterExchange)
                .deadLetterRoutingKey("topic-dead-letter-exchange-key")
                .build();
    }


    /**
     * 生成TopicExchange
     * @return
     */
    @Bean
    public TopicExchange testTopicExchange(){
        return new TopicExchange(RabbitConstant.TEST_TOPIC_EXCHANGE,true,false);
    }

    /**
     * 绑定实例生成
     * @return
     */
    @Bean
    public Binding bindTopic(Queue testTopicQueue,Exchange testTopicExchange){
        return  BindingBuilder.bind(testTopicQueue).to(testTopicExchange).with(RabbitConstant.TEST_TOPIC_ROUTING_KEY).noargs();
    }

}
