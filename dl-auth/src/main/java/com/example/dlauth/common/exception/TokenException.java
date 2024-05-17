package com.example.dlauth.common.exception;

public class TokenException extends DanlabException {

    public TokenException(ExceptionMessage message) {
        super(message.getText());
    }
}
