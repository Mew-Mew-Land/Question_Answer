package org.exmple.mysqlbatis.exception;

import org.exmple.mysqlbatis.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result ex(Exception e){
        System.out.println("wowowowwowow"+e.toString());
        System.out.println("wowowowwowow"+e.getMessage());
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}
