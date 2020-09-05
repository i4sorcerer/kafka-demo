package kafka.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 消息生产者
 *
 * @author : sk
 */
public class KafkaProducerDemo extends Thread {

    private final KafkaProducer<Integer,String> producer;
    private final String topic;

    @Override
    public void run() {
        int num = 0;
        while (num<50){
            String message = "sendmsg_" + num;
            producer.send(new ProducerRecord<Integer, String>(topic,message));
            System.out.println("sending msg:" + message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num++;
        }
    }

    public KafkaProducerDemo(String topic){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "192.168.146.128:9092，192.168.146.128:9093，192.168.146.128:9094");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG,"KafkaProducerDemo");
        properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        this.producer= new KafkaProducer<Integer, String>(properties);
        this.topic = topic;

    }
    public static void main(String[] args) {
        new KafkaProducerDemo("demo-topic").start();
    }
}
