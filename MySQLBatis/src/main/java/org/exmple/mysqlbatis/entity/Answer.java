package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private int ID;
    private int questionId;
    private int answerer;//回答者ID
    private String content;
    private  int commentNum;
    private LocalDateTime updateTime;

    public Answer(int ID, int questionId, int answerer, String content, int commentNum, LocalDateTime updateTime) {
        this.ID = ID;
        this.questionId = questionId;
        this.answerer = answerer;
        this.content = content;
        this.commentNum = commentNum;
        this.updateTime = updateTime;
    }
}
