package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;

import java.util.List;

public interface QuestionService extends ServiceForRedis{
    String QuestionIdPrefix="questionId:";
    String PrefixQuestion="question:";
    String PrefixQuestionViewNum="questionViewNum:";//后面跟id
    PageBean<Question> ProvideQuestion(List<Question> quesS);
    Question createQuestion(Question ques);

    Question searchByID(int id);

    Question modifyQues(Question ques);

    void addNumForQues(int id);

    List<Question> getFaqList(int id);

    List<Question> getAllFaqList();

    Question getQuestionDetail(int id);

    void deleteQuestion(int id);

    List<Question> getQuesListByUser(int id);

}
