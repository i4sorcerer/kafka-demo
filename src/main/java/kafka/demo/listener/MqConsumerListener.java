package kafka.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * mq的消费者监听器
 *
 * @author : sk
 */
@Component
@Slf4j
public class MqConsumerListener extends AbstractMqListener {

    @JmsListener(destination = "${demo.mq.queue.receive1}", concurrency = "2")
    @Override
    public void receive(Message msg) throws Exception {
        ActiveMQTextMessage textMessage = (ActiveMQTextMessage) msg;
        if (textMessage != null) {
            try {
                String text = textMessage.getText();
                // 模拟业务处理，睡5s
                Thread.sleep(5000);
                log.info(Thread.currentThread().getName() + " receive mq message success [" + text + "]");
            } catch (JMSException e) {
                log.error("receive mq message failed ", e);
                Thread.sleep(2000);
                throw new RuntimeException();
            }
        }
    }
}
