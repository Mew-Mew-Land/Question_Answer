package org.exmple.mysqlbatis.controller;
import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.UserServant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.JwkSetUriJwtDecoderBuilderCustomizer;
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
        if(aa!=null){
            return Result.success(userservant.createToken(aa));
        }else return Result.error("账号或密码错误");
    }
}
