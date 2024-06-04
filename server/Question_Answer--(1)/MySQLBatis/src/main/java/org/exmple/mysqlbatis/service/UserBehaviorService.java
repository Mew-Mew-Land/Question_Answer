package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.entity.Question;

public interface UserBehaviorService extends ServiceForRedis{
    int QuestionAttach(Question ques);
}
