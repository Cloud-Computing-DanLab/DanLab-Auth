package com.example.dlauth.api.service.oauth;

import com.example.dlauth.api.dto.LoginPageResponse;
import com.example.dlauth.api.dto.OAuthLoginResponse;
import com.example.dlauth.api.service.oauth.factory.OAuthFactory;
import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import com.example.dlauth.api.service.oauth.factory.adapter.google.GoogleAdapter;
import com.example.dlauth.api.service.oauth.factory.adapter.kakao.KakaoAdapter;
import com.example.dlauth.api.service.oauth.factory.builder.OAuthURLBuilder;
import com.example.dlauth.api.service.oauth.factory.builder.google.GoogleURLBuilder;
import com.example.dlauth.api.service.oauth.factory.builder.kakao.KakaoURLBuilder;
import com.example.dlauth.domain.constant.PlatformType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dlauth.domain.constant.PlatformType.GOOGLE;
import static com.example.dlauth.domain.constant.PlatformType.KAKAO;

@Slf4j
@Service
public class OAuthLoginService {

    // 각 플랫폼에 해당하는 Factory 객체를 매핑해 관리한다.
    private Map<PlatformType, OAuthFactory> adapterMap;

    // 플랫폼별 Adapter, URLBuilder 등록
    public OAuthLoginService(GoogleAdapter googleAdapter, GoogleURLBuilder googleURLBuilder ,
                             KakaoAdapter kakaoAdapter, KakaoURLBuilder kakaoURLBuilder) {
        this.adapterMap = new HashMap<>() {{

            // 카카오 플랫폼 추가
            put(KAKAO, OAuthFactory.builder()
                    .oAuthAdapter(kakaoAdapter)
                    .oAuthURLBuilder(kakaoURLBuilder)
                    .build());

            // 구글 플랫폼 추가
            put(GOOGLE, OAuthFactory.builder()
                    .oAuthAdapter(googleAdapter)
                    .oAuthURLBuilder(googleURLBuilder)
                    .build());

        }};
    }

    // OAuth 2.0 로그인 페이지 생성
    public List<LoginPageResponse> loginPage(String state) {
        // 지원하는 모든 플랫폼의 로그인 페이지를 생성해 반환한다.
        List<LoginPageResponse> urls = adapterMap.keySet().stream()
                .map(type -> {
                    // 각 플랫폼에 해당하는 OAuthFactory 획득
                    OAuthFactory oAuthFactory = adapterMap.get(type);

                    // URL 빌더를 사용해 로그인 페이지 URL 생성
                    String loginPage = oAuthFactory.getOAuthURLBuilder().authorize(state);

                    // 로그인 페이지 DTO 생성
                    return LoginPageResponse.builder()
                            .platformType(type)
                            .url(loginPage)
                            .build();
                })
                .collect(Collectors.toList());

        return urls;
    }

    public OAuthLoginResponse login(PlatformType platformType, String code, String state) {

        OAuthFactory factory = adapterMap.get(platformType);

        OAuthURLBuilder urlBuilder = factory.getOAuthURLBuilder();
        OAuthAdapter adapter = factory.getOAuthAdapter();
        log.info("[DL INFO]: {} Login Start", platformType);

        // code, state를 이용해 Access Token 요청 URL 생성
        String token = urlBuilder.token(code, state);

        // Access Token 획득
        String accessToken = adapter.getToken(token);

        // 사용자 프로필 조회
        OAuthLoginResponse userInfo = adapter.getProfile(accessToken);
        log.info("[DL INFO]: {} Login Success", platformType);

        return userInfo;
    }
}
