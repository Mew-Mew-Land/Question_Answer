package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.exception.AccountException;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServant implements UserService{
    @Autowired
    private UserMapper usermapper;
    @Override
    public User login(User user){
        return usermapper.getByUsernameAndPassword(user);
    }
    @Override
    public String createToken(User user){
        Map<String,Object> claim=new HashMap<>();
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
}
