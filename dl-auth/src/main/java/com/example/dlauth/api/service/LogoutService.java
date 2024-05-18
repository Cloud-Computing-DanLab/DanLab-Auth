package com.example.dlauth.api.service;

import com.example.dlauth.common.exception.ExceptionMessage;
import com.example.dlauth.common.exception.TokenException;
import com.example.dlauth.common.util.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        String jwtToken = AuthenticationExtractor.extractToken(request)
                .orElseThrow(() -> {
                    log.error("[DL ERROR]: {}", ExceptionMessage.JWT_NOT_FOUND.getText());
                    return new TokenException(ExceptionMessage.JWT_NOT_FOUND);
                });

        String subject = jwtTokenProvider.extractSubject(jwtToken);

        log.info("[DL INFO]: {} 님이 로그아웃하셨습니다.", subject);

    }

}
