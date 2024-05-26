package com.example.dlauth.api.service.oauth;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.google.GoogleAdapter;
import com.example.dlauth.api.service.oauth.factory.adapter.kakao.KakaoAdapter;
import com.example.dlauth.api.service.oauth.factory.builder.google.GoogleURLBuilder;
import com.example.dlauth.api.service.oauth.factory.builder.kakao.KakaoURLBuilder;
import com.example.dlauth.common.exception.OAuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static com.example.dlauth.domain.constant.PlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OAuthServiceTest extends IntegrationHelper {
    @Autowired
    private OAuthLoginService oAuthService;

    @Autowired
    private GoogleURLBuilder googleUrlBuilder;

    @MockBean
    private GoogleAdapter googleAdapter;

    @Autowired
    private KakaoURLBuilder kakaoUrlBuilder;

    @MockBean
    private KakaoAdapter kakaoAdapter;

    @Test
    @DisplayName("모든 플랫폼의 로그인 페이지를 성공적으로 반환한다.")
    void allUrlBuilderSuccess() {
        // given
        String state = "test state";

        // when
        List<LoginPageResponse> loginPages = oAuthService.loginPage(state);
        String authorizeUrlGoogle = googleUrlBuilder.authorize(state);
        String authorizeUrlKakao = kakaoUrlBuilder.authorize(state);

        // then
        assertThat(loginPages).hasSize(2); // 리스트 크기 확인

        // 각 플랫폼별 URL인지 확인
        boolean containsGoogle = loginPages.stream()
                .anyMatch(page -> page.platformType().equals(GOOGLE) &&
                        page.url().equals(authorizeUrlGoogle));
        boolean containsKakao = loginPages.stream()
                .anyMatch(page -> page.platformType().equals(KAKAO) &&
                        page.url().equals(authorizeUrlKakao));

        assertThat(containsGoogle).isTrue();
        assertThat(containsKakao).isTrue();
    }

    @Test
    @DisplayName("구글 로그인에 성공하면 OAuthResponse 객체를 반환한다.")
    void googleLoginSuccess() {
        // given
        String code = "valid-code";
        String state = "valid-state";

        OAuthLoginResponse response = OAuthLoginResponse.builder()
                .platformId("1")
                .platformType(GOOGLE)
                .name("이주성")
                .build();

        // when
        // when 사용시 Mockito 패키지 사용
        when(googleAdapter.getToken(any(String.class))).thenReturn("access-token");
        when(googleAdapter.getProfile(any(String.class))).thenReturn(response);
        OAuthLoginResponse profile = oAuthService.login(GOOGLE, code, state);

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "name")
                .contains("1", GOOGLE, "이주성");


    }

    @Test
    @DisplayName("구글 로그인에 실하면 OAuthException 예외가 발생한다.")
    void googleLoginFail() {
        // given
        String code = "invalid-code";
        String state = "invalid-state";

        // when
        when(googleAdapter.getToken(any(String.class))).thenThrow(OAuthException.class);
        assertThrows(OAuthException.class,
                () -> oAuthService.login(GOOGLE, code, state));

    }

    @Test
    @DisplayName("카카오 로그인에 성공하면 OAuthResponse 객체를 반환한다.")
    void kakaoLoginSuccess() {
        // given
        String code = "valid-code";
        String state = "valid-state";

        OAuthLoginResponse response = OAuthLoginResponse.builder()
                .platformId("1")
                .platformType(KAKAO)
                .name("이주성")
                .build();

        // when
        // when 사용시 Mockito 패키지 사용
        when(kakaoAdapter.getToken(any(String.class))).thenReturn("access-token");
        when(kakaoAdapter.getProfile(any(String.class))).thenReturn(response);
        OAuthLoginResponse profile = oAuthService.login(KAKAO, code, state);

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "name")
                .contains("1", KAKAO, "이주성");


    }

    @Test
    @DisplayName("카카오 로그인에 실하면 OAuthException 예외가 발생한다.")
    void kakaoLoginFail() {
        // given
        String code = "invalid-code";
        String state = "invalid-state";

        // when
        when(kakaoAdapter.getToken(any(String.class))).thenThrow(OAuthException.class);
        assertThrows(OAuthException.class,
                () -> oAuthService.login(KAKAO, code, state));

    }
}