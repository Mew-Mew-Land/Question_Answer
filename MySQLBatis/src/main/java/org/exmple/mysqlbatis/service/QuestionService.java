package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;

import java.util.List;

public interface QuestionService extends ServiceForRedis{
    String QuestionPrefix="question:";
    PageBean<Question> ProvideQuestion(List<Question> quesS);
    void createQuestion(Question ques);

    Question searchByID(int id);
}
