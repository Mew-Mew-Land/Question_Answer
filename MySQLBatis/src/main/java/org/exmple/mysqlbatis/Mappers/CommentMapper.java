package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.exmple.mysqlbatis.entity.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> getComment(Integer answerId);
}