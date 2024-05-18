package com.example.dlauth.api.service.oauth.factory.adapter.kakao;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.client.kakao.KakaoProfileClients;
import com.example.dlauth.api.service.oauth.client.kakao.KakaoTokenClients;
import com.example.dlauth.api.service.oauth.client.kakao.response.KakaoProfileResponse;
import com.example.dlauth.api.service.oauth.client.kakao.response.KakaoTokenResponse;
import com.example.dlauth.api.service.oauth.factory.builder.kakao.KakaoURLBuilder;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.OAuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;

import static com.example.dlauth.domain.constant.PlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KakaoAdapterTest extends IntegrationHelper {
    @Autowired
    private KakaoAdapter kakaoAdapter;

    @Autowired
    private KakaoURLBuilder kakaoURLBuilder;

    @Test
    @DisplayName("access 토큰 발급 성공 테스트")
    void kakaoAdapterGetTokenSuccess() {
        // given
        KakaoAdapterTest.MockKakaoTokenClients mockKakaoTokenClients = new KakaoAdapterTest.MockKakaoTokenClients();
        KakaoAdapterTest.MockKakaoProfileClients mockKakaoProfileClients = new KakaoAdapterTest.MockKakaoProfileClients();
        KakaoAdapter kakaoAdapter = new KakaoAdapter(mockKakaoTokenClients, mockKakaoProfileClients);

        // when
        String accessToken = kakaoAdapter.getToken("tokenUrl");

        // then
        System.out.println("accessToken = " + accessToken);
        assertThat(accessToken).isEqualTo("access-token");

    }

    @Test
    @DisplayName("사용자 프로필 조회 성공 테스트")
    void kakaoAdapterGetProfileSuccess() {
        // given
        KakaoAdapterTest.MockKakaoTokenClients mockKakaoTokenClients = new KakaoAdapterTest.MockKakaoTokenClients();
        KakaoAdapterTest.MockKakaoProfileClients mockKakaoProfileClients = new KakaoAdapterTest.MockKakaoProfileClients(1L,
                new KakaoProfileResponse.Properties("이주성", "www.naver.com"));
        KakaoAdapter kakaoAdapter = new KakaoAdapter(mockKakaoTokenClients, mockKakaoProfileClients);

        // when
        OAuthLoginResponse profile = kakaoAdapter.getProfile("access-token");

        // then
        assertAll(
                () -> assertThat(profile.platformId()).isEqualTo("1"),
                () -> assertThat(profile.platformType()).isEqualTo(KAKAO)
        );
    }

    @Test
    @DisplayName("토큰 요청 실패 테스트")
    void kakaoAdapterGetTokenFail() {
        // given
        String tokenURL = kakaoURLBuilder.token("error-token", "state");

        // when
        OAuthException exception = assertThrows(OAuthException.class,
                () -> kakaoAdapter.getToken(tokenURL));

        // then
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.OAUTH_INVALID_TOKEN_URL.getText());

    }

    @Test
    @DisplayName("사용자 프로필 요청 실패 테스트")
    void kakaoAdapterGetProfileFail() {
        // when
        OAuthException exception = assertThrows(OAuthException.class,
                () -> kakaoAdapter.getProfile("error-token"));

        // then
        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.OAUTH_INVALID_ACCESS_TOKEN.getText());

    }

    static class MockKakaoTokenClients implements KakaoTokenClients {

        @Override
        public KakaoTokenResponse getToken(URI uri) {
            return new KakaoTokenResponse("access-token");
        }
    }

    static class MockKakaoProfileClients implements KakaoProfileClients {
        private Long id;
        private KakaoProfileResponse.Properties properties;
        MockKakaoProfileClients(Long id, KakaoProfileResponse.Properties properties){
            this.id = id;
            this.properties = properties;
        }
        MockKakaoProfileClients(){};
        public static class Properties {
            private String nickname;
            private String profile_image;

            public Properties(String nickname, String profile_image) {
                this.nickname = nickname;
                this.profile_image = profile_image;
            }
        }
        @Override
        public KakaoProfileResponse getProfile(String header) {
            return new KakaoProfileResponse(id,
                    new KakaoProfileResponse.Properties(
                            properties.getNickname(),
                            properties.getProfile_image()));
        }
    }
}