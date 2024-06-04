package org.exmple.mysqlbatis.exception;

public class AccountExistException extends AccountException{
 @Override
 public String toString(){
    return "账号已被注册";
 }
}
