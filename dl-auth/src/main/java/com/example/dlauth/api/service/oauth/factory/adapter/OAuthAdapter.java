package com.example.dlauth.api.service.oauth.factory.adapter;


import com.example.dlauth.api.dto.OAuthLoginResponse;

public interface OAuthAdapter {

    // OAuth Access Token 요청 메서드
    String getToken(String tokenURL);

    // OAuth 인증이 완료된 사용자의 프로필 요청 메서드
    OAuthLoginResponse getProfile(String accessToken);
}
