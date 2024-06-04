package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.AnswerMapper;
import org.exmple.mysqlbatis.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class AnswerServant {

    @Autowired
    private AnswerMapper answerMapper;

    public List<Answer> getAnswer(Integer questionId) {

        return answerMapper.getAnswer(questionId);
    }

    public void answerQues(Answer answer) {
        answerMapper.answerQuestion(answer);
    }

    public void deleteAnswer(Answer answer) {
        answerMapper.deleteAnswer(answer);
    }

    public void updateAnswer(Answer answer) {
//        int commentNum= answer.getCommentNum();
//        answer.setCommentNum(++commentNum);
        answerMapper.updateAnswer(answer);
    }
}
