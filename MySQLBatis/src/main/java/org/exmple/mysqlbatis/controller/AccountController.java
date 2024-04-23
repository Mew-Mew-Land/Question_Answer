package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.UserServant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class AccountController {
    @Autowired
    UserServant userservant;
    @PostMapping("/account/login")
    public Result login(@RequestBody User user){
        log.info("用户登录:{}",user);
        User aa=userservant.login(user);
        return aa!=null?Result.success():Result.error("用户名或密码错误");
    }
}
