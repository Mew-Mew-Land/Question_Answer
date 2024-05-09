package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.exception.AccountException;

import java.util.List;

@Mapper
public interface UserMapper {

    //列出所有用户
     List<User> userList();


     //通过账号密码查找
     @Select("select id, username, AccountName, avatar from users where username=#{username} and password=#{password}")
     User getByUsernameAndPassword(User user);

     //批量创建user
     boolean createUserAccount(List<User> users);

     //创建新账号
     @Insert("Insert into users (username,password) values(#{username},#{password})")
     void Insert(User user)throws AccountException;
     //批量删除
     void deleteUser(List<Integer> ids);
     //
     @Select("select AccountName from users where AccountName=#{accountname}")
     User getByAccountName(User user);
}
