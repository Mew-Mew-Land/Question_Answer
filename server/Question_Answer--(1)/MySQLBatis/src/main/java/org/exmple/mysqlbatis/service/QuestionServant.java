package org.exmple.mysqlbatis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.exmple.mysqlbatis.Mappers.QuestionMapper;
import org.exmple.mysqlbatis.entity.PageBean;
import org.exmple.mysqlbatis.entity.Question;
import org.exmple.mysqlbatis.exception.QuestionException;
import org.exmple.mysqlbatis.utils.RedisUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class QuestionServant implements QuestionService{
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private RedisUtil redisUitl;
    @Override
    public PageBean<Question> ProvideQuestion(List<Question> quesS){
        return new PageBean<Question>(quesS);
    }
    @Override
    public Question createQuestion(Question ques)throws QuestionException{
        try {
            questionMapper.insertQuestion(ques);
        }catch(QuestionException e){
            throw new QuestionException("已经存在相同问题");
        }
        return searchByID(ques.getId());
    }
    @Override
    /*
     * 根据ID去找question，并加入缓存(存两份，一份key为id，另一份key为问题)
     *如果数据库中没有，缓存中标记为Catch_NULL
     * */
    public Question searchByID(int id){
       // Question res= questionMapper.getByQuesID(id);
/*        String key=QuestionIdPrefix+id;
        synchronized (this) {
            if (!redisUitl.keyExists(key)) {//不存在这个键，从数据库找到并存入缓存
                res = questionMapper.getByQuesID(id);
                if (res != null) {
                    String value= JSON.toJSONString(res, SerializerFeature.UseISO8601DateFormat);
                    setAndespireKey(key,value);
                    setAndespireKey(PrefixQuestion+res.getQuestion(),value);
                } else {
                    setAndespireKey(key, Catch_NULL);
                }
            } else {
                redisUitl.expire(key,Cache_Time+random(100), TimeUnit.SECONDS);
                res = JSON.parseObject(redisUitl.get(key), Question.class);
            }
        }*/
            return questionMapper.getByQuesID(id);
    }

    @Override
    public Question modifyQues(Question ques) {
        try {
            questionMapper.updateQuestion(ques);
        }catch(QuestionException e){
            throw new QuestionException("修改失败");
        }
        return searchByID(ques.getId());
    }

    @Override
    public void addNumForQues(int id) {
        String key=QuestionIdPrefix+id;
        synchronized (this){
            redisUitl.delete(key);
            questionMapper.updateViewNum(id);
           Question ques = questionMapper.getByQuesID(id);
           String question=JSON.toJSONString(ques);
           setAndespireKey(key,question);
           setAndespireKey(PrefixQuestion+ques.getQuestion(),question);
        }
    }

    @Override
    public List<Question> getFaqList(int id) {
        return questionMapper.getFaqList(id);
    }

    @Override
    public List<Question> getAllFaqList() {
        return questionMapper.getAllFaqList();
    }

    @Override
    public Question getQuestionDetail(int id) {
        return questionMapper.getQuestionDetail(id);
    }

    @Override
    public void deleteQuestion(int id) {
        questionMapper.deleteCommentsOfQuestionAnswer(id);
        questionMapper.deleteAnswerOfQuestion(id);
        questionMapper.deleteQuestion(id);


    }

    @Override
    public List<Question> getQuesListByUser(int id) {
        return questionMapper.getQuesListByUser(id);
    }

    @Override
    public void setAndespireKey(String key, String value) {
        redisUitl.set(key, value,Cache_Time+random(100), TimeUnit.SECONDS);
    }
}
