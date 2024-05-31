package org.exmple.mysqlbatis.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserBehavior {
    private int userID;
    private String questionArr;
    private String isAnsArr;
    private String isComArr;
    private List<Integer> questions;
    private List<Integer> isAns;
    private List<Integer> inCom;

    public UserBehavior(int userID, String questionArr, String isAnsArr, String isComArr) {
        this.userID = userID;
        this.questionArr = questionArr;
        this.isAnsArr = isAnsArr;
        this.isComArr = isComArr;
        this.questions=getResult(questionArr);
        this.isAns=getResult(isAnsArr);
        this.inCom=getResult(isComArr);
    }
    private @NotNull List<Integer> getResult(@NotNull String str){
        List<Integer> resultList = new ArrayList<>();
            String[] numbers = str.split("\\$");
            for(String numStr : numbers) {
                try {
                    int num = Integer.parseInt(numStr);
                    resultList.add(num);
                } catch (NumberFormatException e) {
                    // Skip invalid integers
                    System.out.println("Invalid integer: " + numStr);
                }
            }
            return resultList;
    }
}
