package com.example.dlauth.common.exception;

public class OAuthException extends DanlabException {
    public OAuthException(ExceptionMessage message) {
        super(message.getText());
    }
}
