package com.example.dlauth.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // MemberException
    MEMBER_ROLE_NOT_FOUND("회원의 권한 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    MEMBER_STUDENT_CODE_DUPLICATION("이미 존재하는 학번 코드입니다."),

    // TokenException
    JWT_SUBJECT_IS_NULL("JWT 토큰의 식별자가 NULL입니다."),
    JWT_TOKEN_EXPIRED("JWT 토큰이 만료되었습니다."),
    JWT_UNSUPPORTED("지원하지 않는 JWT 토큰입니다."),
    JWT_MALFORMED("JWT 토큰의 형태가 올바르지 않습니다."),
    JWT_SIGNATURE("JWT 토큰의 SIGNATURE가 올바르지 않습니다."),
    JWT_TOKEN_INVALID( "더 이상 사용할 수 없는 JWT 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT("JWT 토큰의 구성 요소가 올바르지 않습니다."),
    JWT_NOT_FOUND("요청의 헤더에서 JWT 토큰을 읽어올 수 없습니다."),


    // OAuthException
    OAUTH_CONFIG_NULL("OAUTH 설정 정보를 찾을 수 없습니다."),
    OAUTH_INVALID_ACCESS_TOKEN("access_token이 올바르지 않습니다."),
    OAUTH_INVALID_TOKEN_URL("token URL이 올바르지 않습니다."),

    // AuthException
    AUTH_DUPLICATE_UNAUTH_REGISTER("이미 가입이 완료된 회원입니다."),
    UNAUTHORIZED_AUTHORITY("회원가입이 되지 않은 회원입니다."),


    ;
    private final String text;
}
