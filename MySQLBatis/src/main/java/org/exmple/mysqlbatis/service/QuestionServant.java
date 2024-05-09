package org.exmple.mysqlbatis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.exmple.mysqlbatis.Mappers.QuestionMapper;
import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class QuestionServant implements QuestionService{
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private RedisUtil redisUitl;
    @Override
    public PageBean<Question> ProvideQuestion(List<Question> quesS){
        return new PageBean<Question>(quesS.size(),quesS);
    }
    @Override
    public void createQuestion(Question ques){
        questionMapper.insertQuestion(ques);
        //存入缓存
        if(!redisUitl.keyExists(QuestionPrefix+ques.getId())||Objects.equals(redisUitl.get(QuestionPrefix+ques.getId()),Catch_NULL)){
            redisUitl.set(QuestionPrefix+ques.getId(), JSON.toJSONString(ques, SerializerFeature.UseISO8601DateFormat),Cache_Time+random(100), TimeUnit.SECONDS);
        }
    }
    @Override
    public Question searchByID(int id){
        Question res;
        String reds= redisUitl.get(QuestionPrefix+id);
        if(reds==null||Objects.equals(redisUitl.get(QuestionPrefix+id),Catch_NULL)){
           res =  questionMapper.getByQuesID(id);
           if(res!=null){
               redisUitl.set(QuestionPrefix+id,JSON.toJSONString(res,SerializerFeature.UseISO8601DateFormat),Cache_Time+random(100),TimeUnit.SECONDS);
               return res;
           }else{
               return null;
           }
        }else{
            return JSON.parseObject(reds,Question.class);
        }

    }
}
