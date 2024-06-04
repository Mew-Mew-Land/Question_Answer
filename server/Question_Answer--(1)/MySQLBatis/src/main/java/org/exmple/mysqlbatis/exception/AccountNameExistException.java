package org.exmple.mysqlbatis.exception;

public class AccountNameExistException extends AccountException{
    @Override
    public String toString(){
        return "用户名已被使用";
    }
}
