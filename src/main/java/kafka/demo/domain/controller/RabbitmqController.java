package kafka.demo.domain.controller;

import kafka.demo.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * RabbitmqController类
 *
 * @author : sk
 */
@RestController
@RequestMapping("/rabbit")
@Slf4j
public class RabbitmqController {

    /**
     * rabbitmq操作模板类
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendMsg(@RequestParam String msg, @RequestParam Integer exType) {
        if (StringUtils.isEmpty(msg)) {
            msg = "default-msg";
        }

        Map<String, String> msgObj = new HashMap<>();
        msgObj.put("messageId", UUID.randomUUID().toString());
        msgObj.put("messageData", msg);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        msgObj.put("createTime", time);
        String exchange = "";
        List<String> routingKey = new ArrayList();
        switch (exType) {
            case 0:
                // direct
                exchange = RabbitConstant.TEST_DX_EXCHANGE;
                routingKey.add(RabbitConstant.TEST_DX_ROUTING_KEY);
                routingKey.add(RabbitConstant.TEST_DX_ROUTING_KEY+".error");
                break;
            case 1:
                // topic
                exchange = RabbitConstant.TEST_TOPIC_EXCHANGE;
                routingKey.add("test.topic.1");
                routingKey.add("test.topic.2");
                routingKey.add("test.topic.3");
                routingKey.add("test.topicerror");

                break;
            case 2:
                // fanout
                exchange = RabbitConstant.TEST_FANOUT_EXCHANGE;
                routingKey.add("");
                break;
            default:
                log.error("交换机类型选择错误exType->" + exType);
                return "交换机类型选择错误exType->" + exType;
        }

        for (int i= 0;i<routingKey.size();i++) {
            String key = routingKey.get(i);
            rabbitTemplate.convertAndSend(exchange, key, msgObj);
            log.info(i + ": mq msg send success exchange->"+exchange+", exType->" + exType + ", msg->" + msg);
        }
        return msg + " send success.";
    }
}
