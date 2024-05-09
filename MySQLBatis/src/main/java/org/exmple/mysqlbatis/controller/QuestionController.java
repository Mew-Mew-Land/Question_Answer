package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.QuestionServant;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class QuestionController {
    @Autowired
    QuestionServant questionServant;

    @PostMapping("/home")
    public Result getContent(@RequestHeader("token") String token){
        User user=TokenUtil.parsePayloadWithUser(token);
        //获取推送算法的结果
        List<Question> questionList=new ArrayList<>();
        PageBean<Question> res=new PageBean<Question>(0,questionList);
        return Result.success(res);
    }

    @PostMapping("/home/submitQues")
    public Result submitQues(@RequestBody Question ques, @RequestHeader("token") String token){
        User user=TokenUtil.parsePayloadWithUser(token);
        log.info(user.toString());
        ques.setUserID(user.getId());
        questionServant.createQuestion(ques);
        Question res=questionServant.searchByID(ques.getId());
        if(res!=null)
        return Result.success(res);
        else return Result.error("提交失败");
    }
}
