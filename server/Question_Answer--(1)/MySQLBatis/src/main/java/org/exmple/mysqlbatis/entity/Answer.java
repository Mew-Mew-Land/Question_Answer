package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private int id;
    private int questionId;
    private String content;
    private  int commentNum;
    private LocalDateTime updateTime;
    private int userId;//回答者ID
    private String username;

    public Answer(int id, int questionId, String content, int commentNum, LocalDateTime updateTime, int userId) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.content = content;
        this.commentNum = commentNum;
        this.updateTime = updateTime;
    }
}
