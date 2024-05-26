package com.example.dlauth.api.controller;

import com.example.dlauth.IntegrationMockHelper;
import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.SignupRequest;
import com.example.dlauth.api.service.AuthService;
import com.example.dlauth.api.service.JwtTokenProvider;
import com.example.dlauth.common.util.TokenUtils;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.example.dlauth.IntegrationMockHelper.NON_ASCII;
import static com.example.dlauth.fixture.MemberFixture.비연구실_학생_생성;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings(NON_ASCII)
class AuthControllerTest extends IntegrationMockHelper {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private AuthService authService;

    private Member member;

    @BeforeEach
    void setup() {
        member = 비연구실_학생_생성();
        memberRepository.save(member);
    }
    @Test
    @DisplayName("회원가입 성공 테스트")
    void registerSuccessTest() throws Exception {
        // given
        Map<String, String> map = TokenUtils.createTokenMap(member);
        String accessToken = jwtService.generateToken(map, member);

        // 유효성 검사 통과하는 request
        SignupRequest request = SignupRequest.builder()
                .name("이주성")
                .department("컴퓨터공학과")
                .studentCode("32183520")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // when
        when(authService.signup(any(Member.class), any(SignupRequest.class))).thenReturn(LoginResponse.builder().build());

        mockMvc.perform(
                        post("/auth/signup")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_code").value(200))
                .andExpect(jsonPath("$.res_msg").value("OK"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void registerFailTest() throws Exception {
        // given
        Map<String, String> map = TokenUtils.createTokenMap(member);
        String accessToken = jwtService.generateToken(map, member);

        // 유효성 검사 통과하는 request
        SignupRequest request = SignupRequest.builder()
                .name("이주성")
                .department("컴퓨터공학과")
//                .studentCode("32183520")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(request);

        // when
        when(authService.signup(any(Member.class), any(SignupRequest.class))).thenReturn(LoginResponse.builder().build());

        mockMvc.perform(
                        post("/auth/signup")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_code").value(400))
                .andExpect(jsonPath("$.res_msg").value("studentCode: 학번을 입력해주세요."))
                .andDo(print());
    }

}