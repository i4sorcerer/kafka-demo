package kafka.demo.listener;

import javax.jms.Message;

/**
 * @author : sk
 */
public abstract class AbstractMqListener {
    /**
     * 共同的接收消息方法
     *
     * @param message
     * @throws Exception
     */
    abstract void receive(Message message) throws Exception;
}
