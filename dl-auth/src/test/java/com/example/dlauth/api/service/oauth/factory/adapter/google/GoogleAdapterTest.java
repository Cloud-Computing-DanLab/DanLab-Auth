package com.example.dlauth.api.service.oauth.factory.adapter.google;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.client.google.GoogleProfileClients;
import com.example.dlauth.api.service.oauth.client.google.GoogleTokenClients;
import com.example.dlauth.api.service.oauth.client.google.response.GoogleProfileResponse;
import com.example.dlauth.api.service.oauth.client.google.response.GoogleTokenResponse;
import com.example.dlauth.api.service.oauth.factory.builder.google.GoogleURLBuilder;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.OAuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GoogleAdapterTest extends IntegrationHelper {
    @Autowired
    private GoogleAdapter googleAdapter;

    @Autowired
    private GoogleURLBuilder googleURLBuilder;

    @Test
    @DisplayName("access 토큰 발급 성공 테스트")
    void googleAdapterGetTokenSuccess() {
        // given
        GoogleAdapterTest.MockGoogleTokenClients mockGoogleTokenClients = new GoogleAdapterTest.MockGoogleTokenClients();
        GoogleAdapterTest.MockGoogleProfileClients mockGoogleProfileClients = new GoogleAdapterTest.MockGoogleProfileClients();
        GoogleAdapter googleAdapter = new GoogleAdapter(mockGoogleTokenClients, mockGoogleProfileClients);

        // when
        String accessToken = googleAdapter.getToken("tokenUrl");

        // then
        System.out.println("accessToken = " + accessToken);
        assertThat(accessToken).isEqualTo("access-token");

    }

    @Test
    @DisplayName("사용자 프로필 조회 성공 테스트")
    void googleAdapterGetProfileSuccess() {
        // given
        GoogleAdapterTest.MockGoogleTokenClients mockGoogleTokenClients = new GoogleAdapterTest.MockGoogleTokenClients();
        GoogleAdapterTest.MockGoogleProfileClients mockGoogleProfileClients = new GoogleAdapterTest.MockGoogleProfileClients("1","이주성", "www.naver.com");
        GoogleAdapter googleAdapter = new GoogleAdapter(mockGoogleTokenClients, mockGoogleProfileClients);

        // when
        OAuthLoginResponse profile = googleAdapter.getProfile("access-token");

        // then
        assertAll(
                () -> assertThat(profile.platformId()).isEqualTo("1"),
                () -> assertThat(profile.platformType()).isEqualTo(GOOGLE)
        );
    }

    @Test
    @DisplayName("토큰 요청 실패 테스트")
    void googleAdapterGetTokenFail() {
        // given
        String tokenURL = googleURLBuilder.token("error-token", "state");

        // when
        OAuthException exception = assertThrows(OAuthException.class,
                () -> googleAdapter.getToken(tokenURL));

        // then
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());

    }

    static class MockGoogleTokenClients implements GoogleTokenClients {
        @Override
        public GoogleTokenResponse getToken(URI uri) {
            return new GoogleTokenResponse("access-token");
        }
    }

    @Test
    @DisplayName("사용자 프로필 요청 실패 테스트")
    void googleAdapterGetProfileFail() {
        // when
        OAuthException exception = assertThrows(OAuthException.class,
                () -> googleAdapter.getProfile("error-token"));

        // then
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());

    }

    static class MockGoogleProfileClients implements GoogleProfileClients {
        private String sub;
        private String name;
        private String picture;

        public MockGoogleProfileClients(String sub, String name, String picture) {
            this.sub = sub;
            this.name = name;
            this.picture = picture;
        }

        MockGoogleProfileClients(){};

        @Override
        public GoogleProfileResponse getProfile(String header) {
            return new GoogleProfileResponse(sub, name, picture);
        }
    }

}