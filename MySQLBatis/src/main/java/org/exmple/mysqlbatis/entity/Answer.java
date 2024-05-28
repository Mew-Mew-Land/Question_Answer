package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private int ID;
    private int questionId;
    private String content;
    private  int commentNum;
    private LocalDateTime updateTime;
    private int userId;//回答者ID
    private String username;

    public Answer(int ID, int questionId, int userId, String content, int commentNum, LocalDateTime updateTime) {
        this.ID = ID;
        this.questionId = questionId;
        this.userId = userId;
        this.content = content;
        this.commentNum = commentNum;
        this.updateTime = updateTime;
    }
}
