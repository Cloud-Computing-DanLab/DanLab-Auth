package com.example.dlauth.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlatformType {
    KAKAO("카카오 로그인"),
    GOOGLE("구글 로그인");

    private final String text;
}
