package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.AnswerMapper;
import org.exmple.mysqlbatis.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
