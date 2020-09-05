package kafka.demo.domain.controller;

import kafka.demo.cache.RedisCacheUtil;
import kafka.demo.domain.dto.User;
import kafka.demo.domain.service.PageRankService;
import kafka.demo.domain.service.TagUserUtil;
import kafka.demo.util.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * kafka与Spring-boot的整合
 *
 * @author : sk
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaTestDemoController {

    @Value("${kafka.consumer.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Autowired
    private TagUserUtil tagUserUtil;

    @Autowired
    private PageRankService pageRankService;

    @Autowired
    private DistributedLock dsLock;

    @GetMapping("/acquirelock")
    public String acquireLock(@RequestParam String lockName) {
        log.info("acquireLock start lockName:" + lockName);
        String id = dsLock.acquireLock(lockName, 15000, 60000);
        log.info("acquireLock end lockName:" + lockName);
        if (id == null) {
            return "acquireLock fail lockId null";
        }else {
            return "acquireLock success lockId id:" +id;
        }
    }

    @GetMapping("/releaselock")
    public String releaseLock(@RequestParam String lockName, @RequestParam String id) {
        log.info("releaseLock start lockName:" + lockName + " id:" + id);
        try {
            dsLock.releaseLockWithLua(lockName, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("releaseLock end lockName:" + lockName + " id:" + id);
        return "releaseLock success lockId :" + id;
    }

    @GetMapping("/tag")
    public String tagUser(@RequestParam String userid) {
        tagUserUtil.tagUserFavorForMovie(userid);
        tagUserUtil.tagUserFavorForMusic(userid);
        return "tag success";
    }

    @GetMapping("/pagerank")
    public String pageRank(@RequestParam Double score, @RequestParam String member) {
        if (pageRankService.addPageRank(score, member)) {
            log.info("pagerank add success ->" + "score:" + score + ",member:" + member);
            return pageRankService.listAll();
        }
        log.info("pagerank add fail ->" + "score:" + score + ",member:" + member);
        return "pagerank fail";
    }

    @GetMapping("/score")
    public String scoreMember(@RequestParam String member) {
        String scoreM = pageRankService.scoreMember(member);
        if (scoreM != null) {
            log.info("pagerank score success ->" + scoreM);
            return scoreM;
        }
        log.info("pagerank add fail ->" + scoreM);
        return " score fail " + member + "-> not found.";
    }

    @GetMapping("/zrem")
    public String remMember(@RequestParam String member) {
        String allMembers = pageRankService.remMember(member);
        if (allMembers != null) {
            return allMembers;
        }
        log.info("pagerank rem fail ->" + member);
        return "pagerank rem fail  ->" + member;
    }

    @GetMapping("/cache")
    public String cacheUser(@RequestParam String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setName(user.getUserId() + "name");
        user.setPass(user.getUserId() + "pass");
        user.setGender("male");
        user.setHeight(180);
        user.setWeight(69);
        user.setPhone(user.getUserId() + "phone");
        redisCacheUtil.setUser(user);

        return user.toString();
    }

    @GetMapping("/produce")
    public String produce(@RequestParam String msg) {
        String message = "test : " + msg;
        // 设置key和value，看kafka是如何进行分区的
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, message, message);
        kafkaTemplate.send(record);
        log.info("已发送消息->(partition:" + record.partition() + " key:" + record.key() + " value:" + record.value());
//        log.info("kafkaConsumerListener->" + ApplicationConfig.getApplication().getBean("kafkaConsumerListener"));
        return "produce success";
    }

    @GetMapping("/consumer")
    public String consumer() {

        return "consumer success";
    }

}
