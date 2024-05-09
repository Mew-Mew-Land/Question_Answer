package org.exmple.mysqlbatis.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
}
