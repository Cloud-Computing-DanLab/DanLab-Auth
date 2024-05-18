package com.example.dlauth.api.service;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.common.util.TokenUtils;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.dlauth.IntegrationHelper.NON_ASCII;
import static com.example.dlauth.fixture.MemberFixture.비연구실_학생_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings(NON_ASCII)
class JwtTokenProviderTest extends IntegrationHelper {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setup() {
        member = 비연구실_학생_생성();
        memberRepository.save(member);
    }

    @Test
    void Jwt_토큰의_모든_Claims_추출() {
        // given
        Map<String, String> map = TokenUtils.createTokenMap(member);

        // when
        String token = jwtTokenProvider.generateToken(map, member);
        Claims claims = jwtTokenProvider.extractAllClaims(token);
        Date expiration = jwtTokenProvider.extractExpiration(token);
        boolean isTokenValid = jwtTokenProvider.isTokenValid(token, member);

        // then
        assertSoftly(softly -> {
            softly.assertThat(claims.getSubject()).isEqualTo(member.getStudentCode());
            softly.assertThat(claims.get("role")).isEqualTo(member.getRole().name());
            softly.assertThat(claims.get("name")).isEqualTo(member.getName());
            softly.assertThat(expiration).isAfter(new Date());
            softly.assertThat(isTokenValid).isTrue();
        });
    }

    @Test
    void 올바르지_않은_Jwt_토큰일_경우_검증에_실패한다() {
        // given
//        Map<String, String> map = TokenUtil.createTokenMap(member);

        // when
        String token = jwtTokenProvider.generateToken(new HashMap<>(), member);
        boolean isTokenValid = jwtTokenProvider.isTokenValid(token, member);

        // then
        assertThat(isTokenValid).isFalse();

    }

    @Test
    void 식별자가_다른_경우_토큰_검증에_실패한다() {
        // given
        Member another = Member.builder()
                .studentCode("another")
                .build();
        Map<String, String> map = TokenUtils.createTokenMap(member);

        // when
        String token = jwtTokenProvider.generateToken(new HashMap<>(), member);
        boolean isTokenValid = jwtTokenProvider.isTokenValid(token, another);

        // then
        assertThat(isTokenValid).isFalse();
    }

}