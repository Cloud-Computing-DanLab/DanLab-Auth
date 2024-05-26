package com.example.dlauth.api.service.oauth.factory.adapter.google;

import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.client.google.GoogleProfileClients;
import com.example.dlauth.api.service.oauth.client.google.GoogleTokenClients;
import com.example.dlauth.api.service.oauth.client.google.response.GoogleProfileResponse;
import com.example.dlauth.api.service.oauth.client.google.response.GoogleTokenResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;


@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleAdapter implements OAuthAdapter {

    private final GoogleTokenClients googleTokenClients;
    private final GoogleProfileClients googleProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            // URL로 액세스 토큰을 요청
            GoogleTokenResponse token = googleTokenClients.getToken(URI.create(tokenURL));

            // 만약 token이 null일 경우 예외처리
            if (token.getAccess_token() == null) {
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
            }

            return token.getAccess_token();

        } catch (RuntimeException e) {
            log.error("[DL INFO] : Google Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthLoginResponse getProfile(String accessToken) {
        try {
            GoogleProfileResponse profile = googleProfileClients.getProfile("Bearer " + accessToken);

            // 액세스 토큰을 사용하여 프로필 정보 요청
            return OAuthLoginResponse.builder()
                    .platformId(profile.getSub())
                    .name(profile.getName())
                    .platformType(GOOGLE)
                    .build();
        } catch (RuntimeException e) {
            log.error("[DL INFO] : Google Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}

