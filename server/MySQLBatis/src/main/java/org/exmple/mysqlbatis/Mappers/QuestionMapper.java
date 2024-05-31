package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.entity.User;

import java.util.List;

@Mapper
public interface QuestionMapper {

    void insertQuestion(Question ques);
    Question getByQuesID(int id);
    Question getByQuestion(Question ques);
    List<Question> getByUserID(int userId);

    //根据算法给出的ids链表来找Question
    List<Question> getByQuesIDInBatch(List<Integer> Ids);

    void updateQuestion(Question ques);

    void updateQuestionInfo(Question ques);

    void updateViewNum(int id);

    List<Question> getFaqList(int id);

    List<Question> getAllFaqList();
}
