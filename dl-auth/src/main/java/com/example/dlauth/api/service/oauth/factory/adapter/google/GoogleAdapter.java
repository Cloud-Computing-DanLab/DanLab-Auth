package com.example.dlauth.api.service.oauth.factory.adapter.google;

import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;


@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAdapter implements OAuthAdapter {

    @Override
    public String getToken(String tokenURL) {
        return null;
    }

    @Override
    public OAuthLoginResponse getProfile(String accessToken) {
        return null;
    }
}

