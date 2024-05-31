package org.exmple.mysqlbatis.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String accountName;
    private int avatar;

    public User(int id, String username, String password, String accountName, int avatar) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountName = accountName;
        this.avatar = avatar;
    }

    public User() {

    }
}
