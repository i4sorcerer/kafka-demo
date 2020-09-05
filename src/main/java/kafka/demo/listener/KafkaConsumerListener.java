package kafka.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.listener.MessageListener;

/**
 * @author : sk
 */
@Slf4j
public class KafkaConsumerListener implements MessageListener<String,String>  {
    @Override
    public void onMessage(ConsumerRecord<String, String> stringStringConsumerRecord) {
//      log.info("收到消息->" +stringStringConsumerRecord.value());
    }
}
