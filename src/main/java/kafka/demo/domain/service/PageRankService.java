package kafka.demo.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * 每个页面权重相关操作服务类（操作redis中key为redis-rank的有序集合）
 *
 * @author : sk
 */
@Service
@Slf4j
public class PageRankService {
    public static final String PAGE_RANK_KEY = "page-rank";
    private static final double MIN_SCORE = 0;
    private static final double MAX_SCORE = 100;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询指定member的score
     * @param member
     * @return
     */
    public String scoreMember(String member){
        String result = null;

        Double score = redisTemplate.opsForZSet().score(PAGE_RANK_KEY,member);
        if (score ==null){
            return result;
        }
        result = "member:" + member+",score:" +String.valueOf(score);
        return result;
    }

    /**
     * 删除指定的member，并返回删除后的所有元素
     *
     * @param member
     * @return
     */
    public String remMember(String member){
        long remCount = redisTemplate.opsForZSet().remove(PAGE_RANK_KEY,member);
        if (remCount >0){
            String allMembers = listAll();
            log.info(member + " rem success ->" +allMembers);
            return allMembers;
        }
        return null;
    }
    /**
     * 为页面添加权重的方法
     *
     * @param score
     * @param member
     * @return
     */
    public boolean addPageRank(double score, String member) {
        boolean r = false;
        redisTemplate.opsForZSet().add(PAGE_RANK_KEY, member, score);
        long count = redisTemplate.opsForZSet().count(PAGE_RANK_KEY, MIN_SCORE, MAX_SCORE);
//        long count = redisTemplate.opsForZSet().range(PAGE_RANK_KEY,0,-1).size();
        if (count > 0) {
            log.info(PAGE_RANK_KEY + " key add success current member count->" + count);
            r = true;
        }
        return r;
    }

    /**
     * 获取所有members
     *
     * @return
     */
    public String listAll(){
        String result = "result";
        Set<Object> rts = redisTemplate.opsForZSet().range(PAGE_RANK_KEY,0,-1);
        if (!CollectionUtils.isEmpty(rts)){
            StringBuilder sb = new StringBuilder();
            for (Iterator<Object> it=rts.iterator();it.hasNext();){
                sb.append(it.next().toString()).append("->");
            }
            result=sb.toString();
        }
        return result;
    }
}
