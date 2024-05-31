package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Answer;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.AnswerServant;
import org.exmple.mysqlbatis.service.UserServant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins="*")
@RestController
public class AnswerController {
    @Autowired
    private AnswerServant answerServant;
    @Autowired
    private UserServant userServant;

    @PostMapping("/home/viewAnswers")
    public Result getAnswer(@RequestBody Question ques){
        log.info("正在查看问题 id:{}",ques.getId());
        List<Answer> answers=answerServant.getAnswer(ques.getId());
        if(answers!=null){
            for(Answer answer : answers){
                int userId = answer.getUserId();
                User user = userServant.searchUser(userId);
                if(user != null){
                    answer.setUsername(user.getUsername());
                }
            }
            return Result.success(answers);
        }else {
            return Result.error("暂时没有回答哦~~~");
        }
    }

    //回答问题
    @PostMapping("/AnswerQues")
    public Result answerQues(@RequestBody() Answer answer){
        log.info("正在进行问题回答");
        answerServant.answerQues(answer);
        return Result.success("成功回答！");
    }

    //删除回答
    @PostMapping("/deleteAnswer")
    public Result deleteAnswer(@RequestBody Answer answer){
        answerServant.deleteAnswer(answer);
        return Result.success("删除成功");
    }

}
