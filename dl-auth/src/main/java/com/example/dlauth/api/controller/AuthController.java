package com.example.dlauth.api.controller;

import com.example.dlauth.api.dto.*;
import com.example.dlauth.api.service.AuthService;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.response.JsonResult;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.PlatformType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.dlauth.domain.constant.MemberRole.UNAUTH;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OAuthLoginService oAuthLoginService;

    @GetMapping("/loginPage")
    public JsonResult<List<LoginPageResponse>> loginPage() {
        String loginState = UUID.randomUUID().toString();

        // OAuth 사용하여 각 플랫폼의 로그인 페이지 URL을 가져와서 state 주입
        List<LoginPageResponse> loginPages = oAuthLoginService.loginPage(loginState);

        return JsonResult.successOf(loginPages);
    }

    @GetMapping("/{platformType}/login")
    public JsonResult<LoginResponse> login(
            @PathVariable("platformType") PlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String loginState) {

        LoginResponse loginResponse = authService.login(platformType, code, loginState);

        return JsonResult.successOf(loginResponse);
    }

    @PostMapping("/signup")
    public JsonResult<?> register(@AuthenticationPrincipal Member member,
                                  @Valid @RequestBody SignupRequest request) {

        return JsonResult.successOf(authService.signup(member, request));
    }

    @GetMapping("/info")
    public JsonResult<MemberInfoResponse> memberInfo(@AuthenticationPrincipal Member member) {

        if (member.getRole() == UNAUTH) {
            log.error(">>>> {} <<<<", ExceptionMessage.UNAUTHORIZED_AUTHORITY);
            return JsonResult.failOf(ExceptionMessage.UNAUTHORIZED_AUTHORITY.getText());
        }

        MemberInfoResponse userInfoResponse = authService.getMemberInfo(member.getPlatformId());

        return JsonResult.successOf(userInfoResponse);
    }

    @GetMapping("/update")
    public JsonResult<?> updateUser(@AuthenticationPrincipal Member member) {

        // Jwt 토큰을 이용해 유저 정보 추출
        MemberInfoResponse memberInfo = authService.getMemberInfo(member.getPlatformId());

        // 수정 페이지에 필요한 정보를 조회해 반환
        return JsonResult.successOf(authService.memberUpdatePage(memberInfo.memberId()));
    }

    @PostMapping("/update")
    public JsonResult<?> updateUser(@AuthenticationPrincipal Member member,
                                    @Valid @RequestBody MemberUpdateRequest request) {

        // Jwt 토큰을 이용해 유저 정보 추출
        MemberInfoResponse memberInfo = authService.getMemberInfo(member.getPlatformId());

        // 회원 정보 수정
        authService.updateMember(memberInfo, request);

        return JsonResult.successOf("User Update Success.");
    }

    // Todo : 학번 중복 검사


}
