package org.exmple.mysqlbatis;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
class MySqlBatisApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
   public void testUser() {
        List<User> aa=userMapper.list();
        aa.forEach(System.out::println);
    }
}
