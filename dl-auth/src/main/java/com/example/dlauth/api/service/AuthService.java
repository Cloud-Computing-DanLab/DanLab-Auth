package com.example.dlauth.api.service;

import com.example.dlauth.api.dto.*;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.MemberException;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.MemberRole;
import com.example.dlauth.domain.constant.PlatformType;
import com.example.dlauth.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final String ROLE_CLAIM = "role";
    private static final String NAME_CLAIM = "name";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthLoginService oAuthLoginService;

    @Transactional
    public LoginResponse login(PlatformType platformType, String code, String state) {
        OAuthLoginResponse loginResponse = oAuthLoginService.login(platformType, code, state);
        String name = loginResponse.name();
        String platformId = loginResponse.platformId();

        System.out.println("platformId = " + platformId);

        log.info("[DL INFO]: {}님이 로그인하셨습니다.", name);

        /*
         * OAuth 로그인 인증을 마쳤으니 우리 애플리케이션의 DB에도 존재하는 사용자인지 확인한다.
         * 회원이 아닐 경우, 즉 회원가입이 필요한 신규 사용자의 경우 OAuthResponse를 바탕으로 DB에 등록해준다.
         */
        Member member = memberRepository.findByPlatformIdAndPlatformType(platformId, platformType)
                .orElseGet(() -> {
                    Member saveUser = Member.builder()
                            .platformId(platformId)
                            .platformType(platformType)
                            .role(MemberRole.UNAUTH)
                            .name(name)
                            .build();

                    log.info("[DL INFO]: UNAUTH 권한으로 사용자를 DB에 등록합니다. 이후 회원가입이 필요합니다.");
                    return memberRepository.save(saveUser);
                });

        /*
            DB에 저장된 사용자 정보를 기반으로 JWT 토큰을 발급
            * JWT 토큰을 요청시에 담아 보내면 JWT 토큰 인증 필터에서 Security Context에 인증된 사용자로 등록
         */
        String jwtToken = generateJwtToken(member);

        // JWT 토큰과 권한 정보를 담아 반환
        return LoginResponse.builder()
                .accessToken(jwtToken)
                .role(member.getRole())
                .build();
    }

    @Transactional
    public LoginResponse signup(Member member, SignupRequest request) {
        Member findMember = memberRepository.findByPlatformId(member.getPlatformId()).orElseThrow(() -> {
            // UNAUTH인 토큰을 받고 회원 탈퇴 후 그 토큰으로 회원가입 요청시 예외 처리
            log.warn("[DL WARN] : Member Not Exist : {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        // UNAUTH 토큰으로 회원가입을 요청했지만 이미 update되어 UNAUTH가 아닌 사용자 예외 처리
        if (findMember.getRole() != MemberRole.UNAUTH) {
            log.warn("[DL WARN] : Not UNAUTH User : {}", ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER.getText());
            throw new MemberException(ExceptionMessage.AUTH_DUPLICATE_UNAUTH_REGISTER);
        }

        // 회원가입 정보 DB 반영
        findMember.signUp(request.name(), request.studentCode(), request.department()
                , request.labRole(), request.labId());

        // JWT 토큰 재발급
        String token = generateJwtToken(findMember);

        return LoginResponse.builder()
                .accessToken(token)
                .role(findMember.getRole())
                .build();

    }

    private String generateJwtToken(Member member) {
        // JWT 토큰 생성을 위한 claims 생성
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, member.getRole().name());
        claims.put(NAME_CLAIM, member.getName());

        // Access Token 생성
        final String jwtAccessToken = jwtTokenProvider.generateToken(claims, member);
        log.info("[DL INFO] 사용자 {}님의 JWT 토큰이 발급되었습니다 ", member.getName());

        return jwtAccessToken;
    }

    public MemberInfoResponse getMemberInfo(String platformId) {
        Member memberInfo = memberRepository.findByPlatformId(platformId)
                .orElseThrow(() -> {
                    log.warn("[DL WARN] User not found with platformId: {}", platformId);
                    throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
                });

        return MemberInfoResponse.of(memberInfo);
    }

    public MemberUpdatePageResponse memberUpdatePage(Long memberId) {
        Member memberInfo = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.warn("[DL WARN] User not found with memberId: {}", memberId);
                    throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
                });

        return MemberUpdatePageResponse.of(memberInfo);
    }
}
