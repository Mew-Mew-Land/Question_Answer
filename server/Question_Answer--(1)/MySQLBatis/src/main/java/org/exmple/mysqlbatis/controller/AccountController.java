package org.exmple.mysqlbatis.controller;
import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.entity.UserBehavior;
import org.exmple.mysqlbatis.service.UserServant;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@CrossOrigin(origins="*")
@RestController
public class AccountController {
    @Autowired
    UserServant userservant;

    @PostMapping("/account/login")
    public Result login(@RequestBody User user){
        User currUser=userservant.login(user);
        log.info("用户登录:{}",currUser);
        if(currUser!=null){
            return Result.success(userservant.createToken(currUser));
        }else return Result.error("账号或密码错误");
    }
    @PostMapping("/account/register")
    public Result register(@RequestBody User user){
        log.info("新用户注册:{}",user);
        if(!userservant.register(user)){
            return Result.error("用户名已存在");
        }
        User currUser=userservant.login(user);
        return Result.success(userservant.createToken(currUser));
    }

}
