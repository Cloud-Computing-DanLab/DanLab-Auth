package com.example.dlauth.api.service.oauth;

import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.domain.constant.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OAuthLoginService {

    public List<LoginPageResponse> loginPage(String state) {

        return null;
    }

    public OAuthLoginResponse login(PlatformType platformType, String code, String state) {

        return null;
    }
}
