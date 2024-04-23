package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServant implements UserService{
    @Autowired
    private UserMapper usermapper;
    @Override
    public User login(User user){
        return usermapper.getByUsernameAndPassword(user);
    }
}
