package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.exmple.mysqlbatis.entity.UserBehavior;

import java.util.List;

@Mapper
public interface UserBehaviorMapper {

    int insertOnlyOne(UserBehavior ub);
    int insertInBatch(List<UserBehavior> ubList);
    @Select("select * from user_behavior")
    List<UserBehavior> getAllUserBehavior();

    int updateOnlyOne(UserBehavior ub);
    int updateInBatch(List<UserBehavior> ubList);
}
