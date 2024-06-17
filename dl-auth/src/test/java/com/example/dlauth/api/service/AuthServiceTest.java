package com.example.dlauth.api.service;

import com.example.dlauth.IntegrationMockHelper;
import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.MemberInfoResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.dto.SignupRequest;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.common.exception.MemberException;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.MemberRole;
import com.example.dlauth.domain.constant.PlatformType;
import com.example.dlauth.domain.repository.MemberRepository;
import com.example.dlauth.fixture.MemberFixture;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static com.example.dlauth.domain.constant.PlatformType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    private MemberRepository memberRepository;

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

        Member member = memberRepository.findByPlatformIdAndPlatformType(platformId, platformType).get();

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

    @Test
    @DisplayName("UNAUTH 미가입자 회원가입 성공 테스트")
    public void registerUnauthUserSuccessTest() {
        String name = "testUser";

        // UNAUTH 사용자 저장
        Member unauthMember = memberRepository.save(MemberFixture.미인증_회원_생성());

        // 회원가입 요청 생성
        SignupRequest request = SignupRequest.builder()
                .name(name)
                .department("컴퓨터공학과")
                .studentCode("32183520")
                .build();

        // when
        LoginResponse response = authService.signup(unauthMember, request);

        Member savedMember = memberRepository.findByPlatformId(unauthMember.getPlatformId()).orElse(null);
        boolean tokenValid = jwtService.isTokenValid(response.accessToken(), savedMember);   // 발행한 토큰 검증

        // then
        assertThat(tokenValid).isTrue();
        assertEquals(savedMember.getRole(), MemberRole.MEMBER);
        assertEquals(savedMember.getStudentCode(), "32183520");
    }

    @Test
    @DisplayName("UNAUTH 미가입자 회원가입 실패 테스트")
    public void registerUnauthUserFailTest() {
        Member member = MemberFixture.비연구실_학생_생성();
        memberRepository.save(member);

        // 회원가입 요청 생성
        SignupRequest request = SignupRequest.builder().build();

        // then
        assertThrows(MemberException.class, () -> {
            authService.signup(member, request);
        });
    }

    @Test
    public void test() {
        Member member = MemberFixture.비연구실_학생_생성();
        memberRepository.save(member);

        MemberInfoResponse myInfoMy = authService.getMyInfoMy();

        System.out.println("myInfoMy.memberId() = " + myInfoMy.memberId());
        System.out.println("myInfoMy.memberId() = " + myInfoMy.name());
    }
}