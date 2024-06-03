package org.exmple.mysqlbatis.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //存入
    public void set(String key, String value, long duration, TimeUnit timeout) {
        redisTemplate.opsForValue().set(key,value,duration,timeout);
    }
    //获取
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    //查找
    public boolean keyExists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public String expire(String key, long duration, TimeUnit timeout){
      return  redisTemplate.opsForValue().getAndExpire(key,duration,timeout);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }
    public void incr(String key){//对这个键值进行自增操作
        redisTemplate.opsForValue().increment(key);
    }
}
