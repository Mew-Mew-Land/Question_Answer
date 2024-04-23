package org.exmple.mysqlbatis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.exmple.mysqlbatis.Mappers.EmployeeMapper;
import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.*;


@SpringBootTest
class MySqlBatisApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    public static final int ACCESS_EXPIRE = 60;

    @Test
   public void testUser() {
    }
    @Test
    public void JwtTest(){
/*       String aaa=TokenUtil.genAccessToken("wwwww");
        System.out.println(aaa);
        Jws<Claims> a=TokenUtil.parseClaim(aaa);
        System.out.println(a);*/
    }

}
