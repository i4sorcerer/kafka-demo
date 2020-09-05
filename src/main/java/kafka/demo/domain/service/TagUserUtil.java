package kafka.demo.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 给特定用户打标签的共同类
 *
 * @author : sk
 */
@Component
@Slf4j
public class TagUserUtil {
    public static final String USER_TAG_FAVOR_MOVIE = "favor.movie";
    public static final String USER_TAG_FAVOR_MUSIC = "favor.music";

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void tagUserFavorForMovie(String userid){
        tagUser(USER_TAG_FAVOR_MOVIE,userid);
    }
    public void tagUserFavorForMusic(String userid){
        tagUser(USER_TAG_FAVOR_MUSIC,userid);
    }
    private void tagUser(String key, String userid){
        redisTemplate.opsForSet().add(key,userid);
        log.info(key +" tag current [" + redisTemplate.opsForSet().members(key)+"]");
        log.info(key + " tag add success->" +userid);
    }
}
