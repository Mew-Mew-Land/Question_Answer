package org.exmple.mysqlbatis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.exmple.mysqlbatis.Mappers.QuestionMapper;
import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.QuestionServant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;


@SpringBootTest
class MySqlBatisApplicationTests {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionServant questionServant;
    @Autowired
    UserMapper userMapper;
    public static final int ACCESS_EXPIRE = 60;

    @Test
   public void testUser() {
        User user=new User();
        user.setUsername("jane_smith");
        user.setPassword("password2");
        User a= userMapper.getByUsernameAndPassword(user);
        System.out.println(a.toString());
    }
    @Test
    public void JwtTest(){
    }
    @Test
    public void redisTest(){
        try {
            Jedis jedis = new Jedis("localhost", 6379);
            jedis.auth("123456");
            String pingResponse = jedis.ping();
            System.out.println("Successfully connected to Redis. Ping response: " + pingResponse);
            jedis.close();
        } catch (Exception e) {
            System.out.println("Failed to connect to Redis: " + e.getMessage());
        }
    }
    @Test
    public void insertQuestion(){
        Question a=new Question();
        a.setQuestion("sdxwd11");
        a.setClassificationId(1);
        a.setUserId(1);
        a.setUpdateTime(LocalDate.now());
        questionMapper.insertQuestion(a);
        System.out.println(JSON.toJSONString((a), SerializerFeature.UseISO8601DateFormat));
       /* Question res=questionServant.searchByID(8);
        System.out.println(JSON.toJSONString((res), SerializerFeature.UseISO8601DateFormat));*/
    }
}
