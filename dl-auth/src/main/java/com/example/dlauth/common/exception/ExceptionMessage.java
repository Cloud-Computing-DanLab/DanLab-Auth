package com.example.dlauth.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // MemberException
    MEMBER_ROLE_NOT_FOUND("회원의 권한 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),

    ;
    private final String text;
}
