package org.exmple.mysqlbatis.exception;

public class AccountDisexistException extends AccountException{
    @Override
    public String toString() {
        return "账户不存在";
    }
}
