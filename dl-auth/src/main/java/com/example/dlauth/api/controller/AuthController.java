package com.example.dlauth.api.controller;

import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.SignupRequest;
import com.example.dlauth.api.service.AuthService;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.common.response.JsonResult;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.PlatformType;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    // Todo : 회원 정보 조회

    // Todo : 회원 정보 수정 페이지

    // Todo : 회원 정보 수정

    // Todo : 학번 중복 검사

}
