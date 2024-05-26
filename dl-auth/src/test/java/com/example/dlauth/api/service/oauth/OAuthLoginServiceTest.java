package com.example.dlauth.api.service.oauth;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.adapter.google.GoogleAdapter;
import com.example.dlauth.api.service.oauth.factory.builder.google.GoogleURLBuilder;
import com.example.dlauth.common.exception.OAuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OAuthServiceTest extends IntegrationHelper {
    @Autowired
    private GoogleURLBuilder urlBuilder;

    @Autowired
    private OAuthLoginService oAuthService;

    @MockBean
    private GoogleAdapter githubAdapter;

    @Test
    @DisplayName("모든 플랫폼의 로그인 페이지를 성공적으로 반환한다.")
    void allUrlBuilderSuccess() {
        // given
        String state = "test state";

        // when
        List<LoginPageResponse> loginPages = oAuthService.loginPage(state);
        String authorizeURL = urlBuilder.authorize(state);

        // then
        assertThat(loginPages.get(0).url()).isEqualTo(authorizeURL);
    }

    @Test
    @DisplayName("구글 로그인에 성공하면 OAuthResponse 객체를 반환한다.")
    void githubLoginSuccess() {
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
        when(githubAdapter.getToken(any(String.class))).thenReturn("access-token");
        when(githubAdapter.getProfile(any(String.class))).thenReturn(response);
        OAuthLoginResponse profile = oAuthService.login(GOOGLE, code, state);

        // then
        assertThat(profile)
                .extracting("platformId", "platformType", "name")
                .contains("1", GOOGLE, "이주성");


    }

    @Test
    @DisplayName("깃허브 로그인에 실하면 OAuthException 예외가 발생한다.")
    void githubLoginFail() {
        // given
        String code = "invalid-code";
        String state = "invalid-state";

        // when
        when(githubAdapter.getToken(any(String.class))).thenThrow(OAuthException.class);
        assertThrows(OAuthException.class,
                () -> oAuthService.login(GOOGLE, code, state));

    }
}