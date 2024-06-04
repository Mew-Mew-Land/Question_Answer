package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Answer;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.AnswerServant;
import org.exmple.mysqlbatis.service.UserServant;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        log.info("getAnswer正在查看问题的回答 id:{}",ques.getId());
        List<Answer> answers=answerServant.getAnswer(ques.getId());
        if(answers!=null){
            for(Answer answer : answers){
                //System.out.println(answer.toString());
                int userId = answer.getUserId();
                User user = userServant.searchUser(userId);

                if(user != null){
                    answer.setUsername(user.getAccountName());
                }
            }
            return Result.success(answers);
        }else {
            return Result.error("暂时没有回答哦~~~");
        }
    }

    //回答问题
    @PostMapping("/AnswerQues")
    public Result answerQues(@RequestBody() Answer answer, @RequestHeader("token") String token){
        User user= TokenUtil.parsePayloadWithUser(token);
        answer.setUserId(user.getId());
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

    //更新回答
    @PostMapping("answer/updateAnswer")
    public Result dupdateAnswer(@RequestBody Answer answer){

        log.info("正在更新问题:{}",answer.getId());
        answerServant.updateAnswer(answer);
        return Result.success("更新问题成功");
    }

}
