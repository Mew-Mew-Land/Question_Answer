package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.CommentMapper;
import org.exmple.mysqlbatis.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServant implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getComment(Integer answerId) {
        return commentMapper.getComment(answerId);
    }
}