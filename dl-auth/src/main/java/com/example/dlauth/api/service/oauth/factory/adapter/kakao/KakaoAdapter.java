package com.example.dlauth.api.service.oauth.factory.adapter.kakao;

import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAdapter implements OAuthAdapter {
    @Override
    public String getToken(String tokenURL) {
        return null;
    }

    @Override
    public OAuthLoginResponse getProfile(String accessToken) {
        return null;
    }
}
