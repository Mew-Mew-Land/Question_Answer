package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.exmple.mysqlbatis.entity.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper {
    List<Answer> getAnswer(Integer questionId) ;

    void answerQuestion(Answer answer);

    void deleteAnswer(Answer answer);
}