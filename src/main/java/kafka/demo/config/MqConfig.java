package kafka.demo.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQConnectionFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * mq配置类
 *
 * @author : sk
 */
@Configuration
public class MqConfig {
    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connect = new ActiveMQConnectionFactory(usrName,password,brokerUrl);
        // 如果prefetchesize过大的话，即便是开了多个消费端监听，也会导致部分消费者空闲
        ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
        prefetchPolicy.setQueuePrefetch(1);
        connect.setPrefetchPolicy(prefetchPolicy);
        return connect;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        // 设置连接工厂类
        containerFactory.setConnectionFactory(connectionFactory);
        // 设置消息转换器，使用默认的
//        containerFactory.setMessageConverter();
        return containerFactory;
    }
}
