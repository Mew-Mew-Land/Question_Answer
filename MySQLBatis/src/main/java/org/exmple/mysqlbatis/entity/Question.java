package org.exmple.mysqlbatis.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Question {
    private int id;
    private String question;
    private String classification;
    private int answerNum;
    private short isSolved;
    private int UserID;
    private int ViewNum;
    private LocalDate UpdateTime;

    public Question(int id, String question, String classification, int answerNum, short isSolved, int userID, int viewNum, LocalDate updateTime) {
        this.id = id;
        this.question = question;
        this.classification = classification;
        this.answerNum = answerNum;
        this.isSolved = isSolved;
        UserID = userID;
        ViewNum = viewNum;
        UpdateTime = updateTime;
    }

    public Question() {
    }
}
