package kafka.demo.cache;

import kafka.demo.domain.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : sk
 */
@Component
@Slf4j
public class RedisCacheUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void setUser(User user){
        // 做一些访问redis设置hash类型的值
        Map<String,Object> fv = new HashMap<>();
        fv.put("name",user.getName());
        fv.put("pass",user.getPass());
        fv.put("phone", user.getPhone());
        fv.put("height",user.getHeight());
        fv.put("weight",user.getWeight());

        log.info("user kafka.demo.cache set -> " + user.toString());
        String key = "user-"+user.getUserId()+"-info";
        redisTemplate.opsForHash().putAll(key, fv);
        redisTemplate.expire(key,10, TimeUnit.SECONDS);
        log.info("key["+key+"] exist:" + redisTemplate.opsForHash().hasKey(key,"name"));
        log.info("key["+key+"] name :" + redisTemplate.opsForHash().get(key,"name"));
    }
}
