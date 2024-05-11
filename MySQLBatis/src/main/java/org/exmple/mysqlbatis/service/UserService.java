package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService extends ServiceForRedis{
    String prefix_AccountName="AccountName:";
    String prefix_UserID="UserID:";
    User login(User user);
    String createToken(User user);
    boolean register(User user);
    User searchUser(String accountName);
    User searchUser(int id);
}
