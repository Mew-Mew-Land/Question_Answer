package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.exmple.mysqlbatis.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from users")
     List<User> list();
}
