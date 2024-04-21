package org.exmple.mysqlbatis.controller;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
public class HellowController {
    @Autowired
    UserMapper userMapper;
    @RequestMapping("/hello")
    public List<User> hello(){
        System.out.println("wwww");
       return userMapper.list();
    }
}
