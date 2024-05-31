package org.exmple.mysqlbatis.exception;

import org.springframework.dao.DuplicateKeyException;

public class QuestionException extends DuplicateKeyException {
    public QuestionException(String msg) {
        super(msg);
    }

    public QuestionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
