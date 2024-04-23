package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {
    User login(User user);
    String createToken(User user);
}
