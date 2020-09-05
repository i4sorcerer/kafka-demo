package kafka.demo.domain.base;

import kafka.demo.listener.KafkaCOnsumerProc;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * kafka消费者配置类
 *
 * @author : sk
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${kafka.consumer.servers}")
    private String servers;
    @Value("${kafka.consumer.enable.auto.commit}")
    private boolean enableAutoCommit;
    @Value("${kafka.consumer.session.timeout}")
    private String sessionTimeout;
    @Value("${kafka.consumer.auto.commit.interval}")
    private String autoCommitInterval;
    @Value("${kafka.consumer.group.id}")
    private String groupId;
    @Value("${kafka.consumer.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.consumer.concurrency}")
    private int concurrency;

    /**
     * 4. 生成消息处理bean（其实就是个Listener）
     *
     * @return
     */
    @Bean
    public KafkaCOnsumerProc consumerProc(){
        return new KafkaCOnsumerProc();
    }
    /**
     * 3. 生成ListenerContainerFactory对象，用来产生ListenerContain对象。
     *    生成的ListenerContainer对象用来创建Consumer（即listener）
     *
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,String> cklContainerFactory= new ConcurrentKafkaListenerContainerFactory<>();
        cklContainerFactory.setConsumerFactory(consumerFactory());
        cklContainerFactory.setConcurrency(concurrency);
        // set poll timeout ms
        cklContainerFactory.getContainerProperties().setPollTimeout(1500);

        return cklContainerFactory;
    }

    /**
     * 2. 通过配置的Consumer参数，来生成默认的consumer工厂bean
     *
     * @return
     */
    private ConsumerFactory<String,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    /**
     * 1. 配置消费者参数，用来生成Consumer工厂
     *
     * @return
     */
    private Map<String, Object> consumerConfig() {
        Map<String,Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,servers);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);

        return properties;
    }
}
