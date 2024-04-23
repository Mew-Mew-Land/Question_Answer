package org.exmple.mysqlbatis;

import org.exmple.mysqlbatis.Mappers.EmployeeMapper;
import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.Employee;
import org.exmple.mysqlbatis.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@SpringBootTest
class MySqlBatisApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    EmployeeMapper employeeMapper;

    @Test
   public void testUser() {
    }

}
