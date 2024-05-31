package org.exmple.mysqlbatis.service;

import com.alibaba.fastjson2.JSON;
import org.exmple.mysqlbatis.Mappers.UserMapper;
import org.exmple.mysqlbatis.entity.Token;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.exception.AccountException;
import org.exmple.mysqlbatis.utils.RedisUtil;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
    /*
    * 创建token，参考数据为id，username，accountName，avatar
    *  */
    public Token createToken(User user){
        Map<String,Object> claim=new HashMap<>();
        claim.put("id",user.getId());
        claim.put("username",user.getUsername());
        claim.put("accountName",user.getAccountName());
        claim.put("avatar",user.getAvatar());
        return new Token(TokenUtil.genAccessToken(claim));
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
    /*
    * 按照账号马甲查找用户,现在缓存中查找
    *
    * */
    public User searchUser(String accountName){
        String key=prefix_AccountName+accountName;
        User user;
        if(redisUitl.keyExists(key)){//缓存中有,则直接返回,并重新延长过期时间
            String value=redisUitl.expire(key,Cache_Time+random(100), TimeUnit.SECONDS);
            if(Catch_NULL.equals(value))
                user=null;
            else
                user= JSON.parseObject(value,User.class);
        }else{
            synchronized (this) {
                user = getUserFromDB(accountName);
            }
        }
        return user;
    }
    @Override
    public User searchUser(int id){
        //String key=prefix_UserID+id;
        User user;
/*        if(redisUitl.keyExists(key)){//缓存中有,则直接返回,并重新延长过期时间
            String value=redisUitl.expire(key,Cache_Time+random(100), TimeUnit.SECONDS);
            if(Catch_NULL.equals(value))
                user=null;
            else
                user= JSON.parseObject(value,User.class);
        }else{
            synchronized (this) {*/
                user = getUserFromDB(id);

        return user;
    }

    @Nullable
    private User getUserFromDB(String accountName) {
        String key=prefix_AccountName+accountName;
        User user=usermapper.getByAccountName(accountName);
        if(user==null)
            redisUitl.set(key,Catch_NULL,Cache_Time+random(100), TimeUnit.SECONDS);
        else
            setAndespireKey(key,JSON.toJSONString(user));
        return user;
    }
    @Nullable
    private User getUserFromDB(int id) {
        String key=prefix_UserID+id;
        User user = usermapper.getByUserID(id);
/*        if(user==null){
            redisUitl.set(key,Catch_NULL,Cache_Time+random(100), TimeUnit.SECONDS);
        }
        else{
            setAndespireKey(key,JSON.toJSONString(user));
        }*/
        return user;
    }

    @Override
    public User getInformation(int userId){return usermapper.getById(userId);}
    @Override
    public void setAndespireKey(String key, String value) {
        redisUitl.set(key, value,Cache_Time+random(100), TimeUnit.SECONDS);
    }

}
