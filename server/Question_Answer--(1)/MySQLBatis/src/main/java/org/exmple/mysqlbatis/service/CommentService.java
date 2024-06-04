package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getComment(Integer answerId);
}