package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.exception.AccountException;
import org.exmple.mysqlbatis.utils.RedisUtil;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.redisson.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServant implements UserService{
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private RedisUtil redisUitl;
    @Override
    public User login(User user){
        return usermapper.getByUsernameAndPassword(user);
    }
    @Override
    public String createToken(User user){
        Map<String,Object> claim=new HashMap<>();
        claim.put("id",user.getId());
        claim.put("username",user.getUsername());
        claim.put("accountName",user.getAccountName());
        claim.put("avatar",user.getAvatar());
        return TokenUtil.genAccessToken(claim);
    }
    @Override
    public boolean register(User user){

        try{
            usermapper.Insert(user);
        }catch(AccountException e){
            e.toString();
            return false;
        }
        return true;
    }
    @Override
    public boolean searchByAccountName(User user){
        if(redisUitl.keyExists(prefix_AccountName+user.getAccountName())&& !Objects.equals(redisUitl.get(prefix_AccountName + user.getAccountName()), Catch_NULL)){
            return true;//如果缓存中有且不为空
        }else{
           User ReUser = usermapper.getByAccountName(user);
           if(ReUser==null){
               redisUitl.set(prefix_AccountName+user.getAccountName(),Catch_NULL,Cache_Time+random(100), TimeUnit.SECONDS);
               return false;
           }
           else{
               redisUitl.set(prefix_AccountName+user.getAccountName(),user.getAccountName(),Cache_Time+random(100), TimeUnit.SECONDS);
               return true;
           }
        }
    }
}
