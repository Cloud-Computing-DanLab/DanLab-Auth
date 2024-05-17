package com.example.dlauth.common.exception;

public class MemberException extends DanlabException {
    public MemberException(ExceptionMessage message) {
        super(message.getText());
    }
}
