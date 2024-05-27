package com.example.dlauth.api.controller;

import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.MemberInfoResponse;
import com.example.dlauth.api.dto.SignupRequest;
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

    // Todo : 회원 정보 수정 페이지


    // Todo : 회원 정보 수정


    // Todo : 학번 중복 검사


}
