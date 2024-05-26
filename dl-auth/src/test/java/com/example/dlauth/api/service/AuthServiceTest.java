package com.example.dlauth.api.service;

import com.example.dlauth.IntegrationMockHelper;
import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.MemberRole;
import com.example.dlauth.domain.constant.PlatformType;
import com.example.dlauth.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest extends IntegrationMockHelper {
    @MockBean
    private OAuthLoginService oAuthService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtService;

    @Autowired
    private MemberRepository userRepository;

    @Test
    @DisplayName("신규 사용자의 경우 UNAUTH 권한으로 DB에 저장된다.")
    void registerUnauthUser() {
        // given
        String code = "code";
        String state = "state";

        OAuthLoginResponse oAuthResponse = OAuthLoginResponse.builder()
                .platformType(GOOGLE)
                .name("이주성")
                .platformId("asdfqwer1234")
                .build();

        PlatformType platformType = oAuthResponse.platformType();
        String platformId = oAuthResponse.platformId();

        when(oAuthService.login(any(PlatformType.class), any(String.class), any(String.class)))
                .thenReturn(oAuthResponse);

        // when
        authService.login(GOOGLE, code, state);

        Member member = userRepository.findByPlatformIdAndPlatformType(platformId, platformType).get();

        // then
        assertThat(member).isNotNull();
        assertThat(member.getRole().name()).isEqualTo(MemberRole.UNAUTH.name());
    }

    @Test
    @DisplayName("OAuth 로그인 인증 완료 후 JWT 토큰이 정상적으로 발급된다.")
    void loginJwtTokenGenerate() {
        // given
        String code = "code";
        String state = "state";

        OAuthLoginResponse oAuthResponse = OAuthLoginResponse.builder()
                .platformType(GOOGLE)
                .name("이주성")
                .platformId("asdfqwer1234")
                .build();

        when(oAuthService.login(any(PlatformType.class), any(String.class), any(String.class)))
                .thenReturn(oAuthResponse);

        // when
        LoginResponse loginResponse = authService.login(GOOGLE, code, state);
//        System.out.println("Access Token: " + loginResponse.getAccessToken());

        // then
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.accessToken()).isNotBlank();
    }

    @Test
    @DisplayName("OAuth 로그인 인증이 완료된 사용자의 JWT 토큰은 알맞은 Claims가 들어있어야 한다.")
    void loginJwtTokenValidClaims() {
        // given
        String code = "code";
        String state = "state";
        String platformId = "1";
        String name = "이주성";

        OAuthLoginResponse oAuthResponse = OAuthLoginResponse.builder()
                .platformType(GOOGLE)
                .name(name)
                .platformId(platformId)
                .build();

        when(oAuthService.login(any(PlatformType.class), any(String.class), any(String.class)))
                .thenReturn(oAuthResponse);

        // when
        LoginResponse loginResponse = authService.login(GOOGLE, code, state);
        String atk = loginResponse.accessToken();
        Claims claims = jwtService.extractAllClaims(atk);

        // then
        assertAll(
                () -> assertThat(claims.get("role")).isEqualTo(MemberRole.UNAUTH.name()),
                () -> assertThat(claims.get("name")).isEqualTo(name)
        );
    }
}