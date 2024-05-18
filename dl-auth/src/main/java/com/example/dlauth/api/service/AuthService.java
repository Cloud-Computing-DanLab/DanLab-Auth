package com.example.dlauth.api.service;

import com.example.dlauth.api.dto.LoginResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.OAuthLoginService;
import com.example.dlauth.domain.constant.PlatformType;
import com.example.dlauth.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final String ROLE_CLAIM = "role";
    private static final String NAME_CLAIM = "name";

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private final OAuthLoginService oAuthLoginService;

    public LoginResponse login(PlatformType platformType, String code, String state) {
        OAuthLoginResponse loginResponse = oAuthLoginService.login(platformType, code, state);

        return null;
    }
}
