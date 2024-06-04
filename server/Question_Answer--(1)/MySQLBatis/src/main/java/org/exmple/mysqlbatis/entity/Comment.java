package org.exmple.mysqlbatis.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private int id;
    private int answerId;
    private String comment;
    private LocalDateTime postingTime;
    private  int userId;
    private String username;

    public Comment(int id, int answerId, int userId, String comment, LocalDateTime postingTime) {
        this.id = id;
        this.answerId = answerId;
        this.userId = userId;
        this.comment = comment;
        this.postingTime = postingTime;
    }
}
