package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Question {
    private int id;
    private String question;
    private int classificationId;
    private int answerNum;
    private short isSolved;
    private int ViewNum;
    private LocalDate updateTime;
    private int userId;
    private String username;

    public Question(int id, String question, int classificationId, int answerNum, short isSolved, int viewNum, LocalDate updateTime, int userId, String username) {
        this.id = id;
        this.question = question;
        this.classificationId = classificationId;
        this.answerNum = answerNum;
        this.isSolved = isSolved;
        ViewNum = viewNum;
        this.updateTime = updateTime;
        this.userId = userId;
        this.username = username;
    }

    public Question() {
    }
}
