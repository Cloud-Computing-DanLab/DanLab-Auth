package com.example.dlauth.config.filter;

import com.example.dlauth.IntegrationHelper;
import com.example.dlauth.api.service.JwtTokenProvider;
import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.util.TokenUtils;
import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.dlauth.fixture.MemberFixture.비연구실_학생_생성;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class SecurityConfigTest extends IntegrationHelper {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 인증_받은_사용자는_모든_엔드포인트에_접근할_수_있다() throws Exception {
        // given
        String uri = "/test";
        String expectBody = "test!!";

        Member savedMember = memberRepository.save(비연구실_학생_생성());
        String jwtToken = jwtTokenProvider.generateToken(TokenUtils.createTokenMap(savedMember), savedMember);

        // when
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectBody))
                .andDo(print());
    }

    @Test
    void 만료된_토큰은_검증에_실패한다() throws Exception {
        // given
        String uri = "/test";

        Member savedMember = memberRepository.save(비연구실_학생_생성());
        String jwtToken = jwtTokenProvider.buildToken(TokenUtils.createTokenMap(savedMember), savedMember, 0L);

        // when
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_code").value(400))
                .andExpect(jsonPath("$.res_msg").value(ExceptionMessage.JWT_TOKEN_EXPIRED.getText()))
                .andDo(print());
    }

}