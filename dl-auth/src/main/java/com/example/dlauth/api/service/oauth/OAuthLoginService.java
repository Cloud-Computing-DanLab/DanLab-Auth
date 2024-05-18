package com.example.dlauth.api.service.oauth;

import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.OAuthFactory;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import com.example.dlauth.api.service.oauth.factory.builder.OAuthURLBuilder;
import com.example.dlauth.domain.constant.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OAuthLoginService {

    // 각 플랫폼에 해당하는 Factory 객체를 매핑해 관리한다.
    private Map<PlatformType, OAuthFactory> adapterMap;

    // 플랫폼별 Adapter, URLBuilder 등록
    public OAuthLoginService() {
        this.adapterMap = new HashMap<>();
    }

    // OAuth 2.0 로그인 페이지 생성
    public List<LoginPageResponse> loginPage(String state) {
        List<LoginPageResponse> urls = new ArrayList<>();

        // 지원하는 모든 플랫폼의 로그인 페이지를 생성해 반환한다.
        for (PlatformType type : adapterMap.keySet()) {
            // 각 플랫폼에 해당하는 OAuthFactory 획득
            OAuthFactory oAuthFactory = adapterMap.get(type);

            // URL 빌더를 사용해 로그인 페이지 URL 생성
            String loginPage = oAuthFactory.getOAuthURLBuilder().authorize(state);

            urls.add(LoginPageResponse.builder()
                    .platformType(type)
                    .url(loginPage)
                    .build());
        }

        return urls;
    }

    public OAuthLoginResponse login(PlatformType platformType, String code, String state) {

        OAuthFactory factory = adapterMap.get(platformType);

        OAuthURLBuilder urlBuilder = factory.getOAuthURLBuilder();
        OAuthAdapter adapter = factory.getOAuthAdapter();
        log.info(">>>> {} Login Start", platformType);

        // code, state를 이용해 Access Token 요청 URL 생성
        String token = urlBuilder.token(code, state);

        // Access Token 획득
        String accessToken = adapter.getToken(token);

        // 사용자 프로필 조회
        OAuthLoginResponse userInfo = adapter.getProfile(accessToken);
        log.info(">>>> {} Login Success", platformType);

        return userInfo;
    }
}
