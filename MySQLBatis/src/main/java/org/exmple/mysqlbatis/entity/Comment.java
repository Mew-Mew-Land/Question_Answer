package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private int ID;
    private int answerID;
    private  int userID;
    private String comment;
    private LocalDateTime postingTime;

    public Comment(int ID, int answerID, int userID, String comment, LocalDateTime postingTime) {
        this.ID = ID;
        this.answerID = answerID;
        this.userID = userID;
        this.comment = comment;
        this.postingTime = postingTime;
    }
}
