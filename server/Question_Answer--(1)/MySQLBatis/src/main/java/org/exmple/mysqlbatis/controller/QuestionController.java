package org.exmple.mysqlbatis.controller;

import io.jsonwebtoken.security.Request;
import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.entity.User;
import org.exmple.mysqlbatis.service.AnswerServant;
import org.exmple.mysqlbatis.service.QuestionServant;
import org.exmple.mysqlbatis.service.UserServant;
import org.exmple.mysqlbatis.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@CrossOrigin(origins="*")
@RestController
public class QuestionController {
    @Autowired
    private QuestionServant questionServant;
    @Autowired
    private AnswerServant answerServant;
    @Autowired
    private UserServant userServant;

    @PostMapping("/home")
    public Result getContent(@RequestHeader("token") String token){
        User user=TokenUtil.parsePayloadWithUser(token);
        //获取推送算法的结果
        List<Question> questionList=new ArrayList<>();
        PageBean<Question> res=new PageBean<Question>(questionList);
        return Result.success(res);
    }

    @PostMapping("/home/submitQues")
    public Result submitQues(@RequestBody Question ques,@RequestHeader("token") String token){
        System.out.println(ques);
        User user=TokenUtil.parsePayloadWithUser(token);
        ques.setUserId(user.getId());
        ques.setUsername(user.getUsername());
        log.info(String.valueOf("quesController.submitQues:"+ques.getUsername()));
        Question res=questionServant.createQuestion(ques);
        if(res!=null)
        return Result.success(res);
        else return Result.error("提交失败");
    }
    @PostMapping("/home/submitQues/modify")
    public Result modifyQues(@RequestBody Question ques){
        Question res=questionServant.modifyQues(ques);
        if(res!=null)
            return Result.success(res);
        else return Result.error("提交失败");
    }
    @PostMapping("/home/submitQues/setIsSolved")
    public Result setIsSolved(@RequestBody int id){
        return Result.success("www"+id);
    }

    @PostMapping("/home/viewQuestion")
    public Result viewQues(@RequestBody int id){
        questionServant.addNumForQues(id);
        return Result.success("成功增加");
    }


    @PostMapping("/question/faqList")//获取某个种类的所有问题
    public Result getFaqList(@RequestBody Question question){
        System.out.println(question.getClassificationId());
        if(question.getClassificationId()==0){
            List<Question> List =questionServant.getAllFaqList();
            return  Result.success(List);
        }
        List<Question> List =questionServant.getFaqList(question.getClassificationId());
        return  Result.success(List);
    }

    @PostMapping("/deleteQuestion")
    public Result deleteQuestion(@RequestBody Question question,@RequestHeader("token") String token){
        User user=TokenUtil.parsePayloadWithUser(token);
        Question question1=questionServant.searchByID(question.getId());

        log.info("deletequestion："+user.getId()+"#"+String.valueOf(question1.getUserId())+"#");
        if(user.getId()==question1.getUserId()) {
            log.info("正在删除问题：{}", question.getId());
            questionServant.deleteQuestion(question.getId());
            return Result.success("删除问题成功！");
        }else return Result.error("这似乎不是您发表的问题");
    }

    @PostMapping("/question/questionDetail")//获取问题的详细信息
    public Result getQuestionDetail(@RequestBody Question question,@RequestHeader("token") String token){
        log.info("正在查看问题:{}",question.getId(),"的全部信息");
        Question ReQuestion= questionServant.getQuestionDetail(question.getId());
        User user=userServant.searchUser(ReQuestion.getUserId());
        ReQuestion.setUsername(user.getUsername());
        return Result.success(ReQuestion);
    }

    @PostMapping("/QuesListByUser")//获取某个人发布的全部信息
    public Result getQuesListByUser(@RequestHeader("token") String token){
        User user=TokenUtil.parsePayloadWithUser(token);
        log.info("正在查询：{}",user.getId(),"发布的所有问题");
        List<Question> List =questionServant.getQuesListByUser(user.getId());
        return  Result.success(List);
    }


}
