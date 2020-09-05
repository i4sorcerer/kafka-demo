package kafka.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * 消息处理类
 *
 * @author : sk
 */
@Slf4j
public class KafkaCOnsumerProc {

    @KafkaListener(topics = "part-topic")
    public void listener(ConsumerRecord<?,?> record){
      log.info("收到待处理消息->(partition:" + record.partition()+" key:" +  record.key() + " value:" + record.value());
    }
}
