package com.example.dlauth.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum MemberRole {
    UNAUTH("미인증"),
    MEMBER("회원");

    private final String role;
}