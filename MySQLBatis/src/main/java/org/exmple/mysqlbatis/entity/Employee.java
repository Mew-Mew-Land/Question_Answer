package org.exmple.mysqlbatis.entity;

import lombok.Data;

@Data
public class Employee {
    private int ID;
    private  String name;
    private  char gender;
    private  int job;

    public Employee(int ID, String name, char gender, int job) {
        this.ID = ID;
        this.name = name;
        this.gender = gender;
        this.job = job;
    }
}
