package kafka.demo.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author : sk
 */
public interface RabbitConstant {
    /**
     * DirectExchange交换机
     */
    String TEST_DX_EXCHANGE = "TEST-DX-EXCHANGE";
    /**
     * DirectExchange客户端监听queue名称 Direct
     */
    String TEST_DX_QUEUE = "TEST-DX-QUEUE";

    /**
     * DirectExchange交换机路由key
     */
    String TEST_DX_ROUTING_KEY = "test.direct";
    String TEST_TOPIC_ROUTING_KEY = "test.topic.*";
    String TEST_TOPIC_ROUTING_KEY2 = "test.topic.#";
    /**
     * topic交换机绑定的queue
     */
    String TEST_TOPIC_QUEUE = "TEST-TOPIC-QUEUE";
    /**
     * topic交换机
     */
    String TEST_TOPIC_EXCHANGE = "TEST_TOPIC_EXCHANGE";
    /**
     * fanout交换机
     */
    String TEST_FANOUT_EXCHANGE = "TEST-FANOUT-EXCHANGE ";
    /**
     * fanout交换机绑定的queue1
     */
    String TEST_FANOUT_QUEUE1 = "TEST-FANOUT-QUEUE1";
    /**
     * fanout交换机绑定的queue2
     */
    String TEST_FANOUT_QUEUE2 = "TEST-FANOUT-QUEUE2";



    String QUEUE_ARG_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    String QUEUE_ARG_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

}
