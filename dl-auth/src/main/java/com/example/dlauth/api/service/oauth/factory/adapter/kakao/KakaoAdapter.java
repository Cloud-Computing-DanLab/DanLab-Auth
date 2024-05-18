package com.example.dlauth.api.service.oauth.factory.adapter.kakao;

import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.client.kakao.KakaoProfileClients;
import com.example.dlauth.api.service.oauth.client.kakao.KakaoTokenClients;
import com.example.dlauth.api.service.oauth.client.kakao.response.KakaoProfileResponse;
import com.example.dlauth.api.service.oauth.client.kakao.response.KakaoTokenResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.OAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

import static com.example.dlauth.domain.constant.PlatformType.KAKAO;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAdapter implements OAuthAdapter {
    private final KakaoTokenClients kakaoTokenClients;
    private final KakaoProfileClients kakaoProfileClients;

    @Override
    public String getToken(String tokenURL) {
        try {
            KakaoTokenResponse token = kakaoTokenClients.getToken(URI.create(tokenURL));
            // URL로 액세스 토큰을 요청

            // 만약 token이 null일 경우 예외처리
            if (token.getAccess_token() == null) {
                throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
            }
            return token.getAccess_token();
        } catch (RuntimeException e) {
            log.error(">>>> [ Kakao Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_TOKEN_URL);
        }
    }

    @Override
    public OAuthLoginResponse getProfile(String accessToken) {
        try {
            KakaoProfileResponse profile = kakaoProfileClients.getProfile("Bearer " + accessToken);

            // 액세스 토큰을 사용하여 프로필 정보 요청
            return OAuthLoginResponse.builder()
                    .platformId(profile.getId().toString())
                    .platformType(KAKAO)
                    .build();
        } catch (RuntimeException e) {
            log.error(">>>> [ Kakao Oauth 인증 에러 발생: {}", ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());
            throw new OAuthException(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN);
        }
    }
}
