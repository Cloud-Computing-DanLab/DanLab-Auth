package com.example.dlauth.api.controller;

import com.example.dlauth.api.dto.*;
import com.example.dlauth.api.service.AuthService;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.response.JsonResult;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.constant.PlatformType;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "로그인페이지 요청")
    @ApiResponse(responseCode = "200", description = "로그인페이지 요청 성공", content = @Content(schema = @Schema(implementation = LoginPageResponse.class)))
    @GetMapping("/loginPage")
    public JsonResult<List<LoginPageResponse>> loginPage() {
        String loginState = UUID.randomUUID().toString();

        // OAuth 사용하여 각 플랫폼의 로그인 페이지 URL을 가져와서 state 주입
        List<LoginPageResponse> loginPages = oAuthLoginService.loginPage(loginState);

        return JsonResult.successOf(loginPages);
    }

    @ApiResponse(responseCode = "200", description = "로그인 요청 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @GetMapping("/{platformType}/login")
    public JsonResult<LoginResponse> login(
            @PathVariable("platformType") PlatformType platformType,
            @RequestParam("code") String code,
            @RequestParam("state") String loginState) {

        LoginResponse loginResponse = authService.login(platformType, code, loginState);

        return JsonResult.successOf(loginResponse);
    }

    @ApiResponse(responseCode = "200", description = "회원가입 요청 성공", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping("/signup")
    public JsonResult<?> register(@AuthenticationPrincipal Member member,
                                  @Valid @RequestBody SignupRequest request) {

        return JsonResult.successOf(authService.signup(member, request));
    }

    @ApiResponse(responseCode = "200", description = "회원 정보 요청 성공", content = @Content(schema = @Schema(implementation = MemberInfoResponse.class)))
    @GetMapping("/info")
    public JsonResult<MemberInfoResponse> memberInfo(@AuthenticationPrincipal Member member) {

//        if (member.getRole() == UNAUTH) {
//            log.error(">>>> {} <<<<", ExceptionMessage.UNAUTHORIZED_AUTHORITY);
//            return JsonResult.failOf(ExceptionMessage.UNAUTHORIZED_AUTHORITY.getText());
//        }

        MemberInfoResponse userInfoResponse = authService.getMyInfo(member.getPlatformId());

        return JsonResult.successOf(userInfoResponse);
    }

    @GetMapping("/info/my")
    public JsonResult<MemberInfoResponse> memberInfoMy() {

//        if (member.getRole() == UNAUTH) {
//            log.error(">>>> {} <<<<", ExceptionMessage.UNAUTHORIZED_AUTHORITY);
//            return JsonResult.failOf(ExceptionMessage.UNAUTHORIZED_AUTHORITY.getText());
//        }

        MemberInfoResponse userInfoResponse = authService.getMyInfoMy();

        return JsonResult.successOf(userInfoResponse);
    }

    @ApiResponse(responseCode = "200", description = "특정 회원 정보 요청 성공", content = @Content(schema = @Schema(implementation = MemberInfoResponse.class)))
    @GetMapping("/info/{memberId}")
    public JsonResult<MemberInfoResponse> memberInfo(@AuthenticationPrincipal Member member,
                                                     @PathVariable(name = "memberId") Long memberId) {

        if (member.getRole() == UNAUTH) {
            log.error(">>>> {} <<<<", ExceptionMessage.UNAUTHORIZED_AUTHORITY);
            return JsonResult.failOf(ExceptionMessage.UNAUTHORIZED_AUTHORITY.getText());
        }

        MemberInfoResponse userInfoResponse = authService.getMemberInfo(memberId);

        return JsonResult.successOf(userInfoResponse);
    }

    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    @PostMapping("/update")
    public JsonResult<?> updateUser(@AuthenticationPrincipal Member member,
                                    @Valid @RequestBody MemberUpdateRequest request) {

        // Jwt 토큰을 이용해 유저 정보 추출
        MemberInfoResponse memberInfo = authService.getMyInfo(member.getPlatformId());

        // 회원 정보 수정
//        authService.updateMember(memberInfo, request);

        return JsonResult.successOf("User Update Success.");
    }

    @ApiResponse(responseCode = "200", description = "학번 중복 체크 성공")
    @PostMapping("/checkCode")
    public JsonResult<?> stdCodeDuplicationCheck(@Valid @RequestBody MemberStudentCheckRequest request) {

        authService.stdCodeDuplicationCheck(request);

        return JsonResult.successOf("studentCode Duplication Check Success.");
    }

}
