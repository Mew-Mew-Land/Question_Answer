package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.UserServant;
import org.exmple.mysqlbatis.service.UserService;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins="*")
@RestController
public class UserController {
    @Autowired
    private UserServant userservant;

    @PostMapping("/account/Info")
    public Result userInfo(@RequestHeader("token") String token){
        User user= TokenUtil.parsePayloadWithUser(token);
        log.info(token);
        log.info("123213123123"+user.toString());
        user=userservant.getInformation(user.getId());
        if(user!=null)return Result.success(user);
        return Result.error("找不到用户信息");
    }

    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestHeader("token") String token){
        User user= TokenUtil.parsePayloadWithUser(token);
        log.info(token);
        log.info("正在更新用户：{}"+user.toString(),"的信息");
        user=userservant.getInformation(user.getId());
        if(user!=null)return Result.success(user);
        return Result.error("找不到用户信息");
    }


}
