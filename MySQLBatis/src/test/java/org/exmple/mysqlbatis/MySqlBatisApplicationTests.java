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
       /* List<Integer> aa= Arrays.asList(1,2,3);
        userMapper.deleteUser(aa);*/
/*        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Alice", 25));
        userList.add(new User(2, "Bob", 30));
        userList.add(new User(3, "Charlie", 28));
        userMapper.createUserAccount(userList);*/
        List<Employee> aa=employeeMapper.searchEmployee(null,null,null,null);
        for (Employee employee : aa) {
            System.out.println(employee);
        }
    }

}
