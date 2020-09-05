package kafka.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分布式锁的自我实现v1.0
 *
 * @author : sk
 */
@Slf4j
@Component
public class DistributedLock {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获得锁
     *
     * @param acquireName    锁名称
     * @param acquireTimeout 获得锁超时时间
     * @param lockTimeout    分布式锁的超时自动释放
     * @return 成功获得锁之后的锁标识
     */
    public String acquireLock(String acquireName, long acquireTimeout, long lockTimeout) {
        String id = UUID.randomUUID().toString();
        // 通过setnx方式去获得锁
        String key = "Lock:" + acquireName;
        long endTIme = System.currentTimeMillis() + acquireTimeout;

        while (System.currentTimeMillis() <= endTIme) {
            Boolean exitFlg = redisTemplate.opsForValue().setIfAbsent(key, id);
            if (exitFlg) {
                // key不存在，则返回true，设置成功，获得锁
                // 设置锁的ttl，防止异常情况下，死锁发生
                redisTemplate.expire(key, lockTimeout, TimeUnit.MILLISECONDS);
                log.info(Thread.currentThread().getName() + " get lock success. key:" + key + " id:" + id);
                return id;
            }
            // 如果不是第一次获得锁，尝试check ttl，如果未设置，则进行设置
            long ttl = redisTemplate.getExpire(key);
            if (ttl == -1) {
                // 未设置ttl
                redisTemplate.expire(key, lockTimeout, TimeUnit.MILLISECONDS);
            }
            // 锁未获得成功情况下，继续获取，知道设置的acquireTimeout到期为止
            log.info(Thread.currentThread().getName() + " get lock fail, try next key:" + key + " id:" + id);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 锁最终获得失败，则返回null
        return null;
    }

    /**
     * 释放获得的锁非事务的方式
     *
     * @param lockName 锁名称
     * @param id       锁标识
     */
    public boolean releaseLockNoTx(String lockName, String id) {
        String lockKey = "lock:" + lockName;
        boolean released = false;
        if (id.equals(redisTemplate.opsForValue().get(lockKey))) {
            // 判断要释放的锁的人是不是获得锁的人
            released = redisTemplate.delete(lockKey);
        }
        return released;
    }

    /**
     * 以事务的方式释放获得的锁
     *
     * @param lockName 锁名称
     * @param id       锁标识
     * @return 释放结果
     */
    public boolean releaseLockWithTx(String lockName, String id) throws Exception {
        String lockKey = "Lock:" + lockName;
        final AtomicBoolean released = new AtomicBoolean(false);
        log.info(lockName+" lock id [" + id+"]");
        String acquiredLockId = (String) redisTemplate.opsForValue().get(lockKey);
        if (id.equals(acquiredLockId)) {
            // 判断要释放的锁的人是不是获得锁的人
//            released = redisTemplate.delete(lockKey);
            redisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    List<Object> rs = null;
                    do {
                        try {
                            redisOperations.watch(lockKey);

                            redisOperations.multi();

                            //在此的所有操作时一个事务
                            redisOperations.delete(lockKey);
                            //提交事务执行
                            rs = redisOperations.exec();
                            released.set(true);
                            redisOperations.unwatch();
                            return rs;
                        } catch (Exception e) {
                            log.error(Thread.currentThread().getName() + "所释放提交事务失败->lockkey:" + lockKey + " id:" + id, e);
                        }
                    } while (rs == null);
                    return null;
                }
            });
        } else {
            // 如果释放锁的人不是获取所得人
            throw new Exception("lock " + lockKey +" can be released only by owner. currThread->" + Thread.currentThread().getName());
        }
        return released.get();
    }

    /**
     * 通过lua脚本释放锁，lua脚本可以保证原子性
     *
     * @param lockName
     * @param id
     * @return
     */
    public boolean releaseLockWithLua(String lockName,String id){
        String lockKey = "Lock:" +lockName;
        String lua = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) " +
                "else return 0 " +
                "end";
        DefaultRedisScript<Long> releaseLockScript = new DefaultRedisScript<>();
        releaseLockScript.setResultType(Long.class);
        releaseLockScript.setScriptText(lua);

        List<String> keys = new ArrayList<>();
        keys.add(lockKey);
        Long rs = redisTemplate.execute(releaseLockScript,keys,id);
        if (rs.intValue() >0){
            log.info("release lock by lua success ");
            return true;
        }
        return false;
    }
}
