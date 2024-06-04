package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Comment;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins="*")
@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/Answer")
    public Result getComment(@RequestBody Integer answerId){
        log.info("正在查看回答 :{}",answerId,"下的问题");
        List<Comment> comments=commentService.getComment(answerId);
        if(comments!=null){
            return Result.success(comments);
        }else{
            return  Result.error("该回答下暂无评论~~~嘤嘤嘤");
        }
    }
}
