package kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author : sk
 */
public class KafkaConsumerDemo extends Thread {
    private final KafkaConsumer<Integer, String> consumer;

    public KafkaConsumerDemo(String topicid) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "192.168.146.128:9092，192.168.146.128:9093，192.168.146.128:9094");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaConsumerDemo2");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<Integer, String>(properties);
        consumer.subscribe(Collections.singletonList(topicid));
    }

    @Override
    public void run() {
        int num = 0;
        while (true) {
//            System.out.println("receive index:" + num);
            ConsumerRecords<Integer, String> records = consumer.poll(1000);
            for (ConsumerRecord record : records) {
                System.out.println("received msg:" + record.value());
//                consumer.commitAsync();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num++;
        }
    }

    public static void main(String[] args) {
        new KafkaConsumerDemo("demo-topic").start();
    }
}
