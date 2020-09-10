package kafka.demo.config.mq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq的基础配置
 *
 * @author : sk
 */
@Configuration
@Slf4j
public class AbstractRabbitmqConfig {
    /**
     * 消费端listenerContainer设置参数
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setPrefetchCount(1);
        return containerFactory;
    }
}
