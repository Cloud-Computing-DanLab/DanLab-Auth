package com.example.dlauth.api.service.oauth.client.kakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoProfileResponse {
    private Long id;

    private Properties properties;

    public KakaoProfileResponse(Long id, Properties properties) {
        this.id = id;
        this.properties = properties;
    }

    @Getter
    @NoArgsConstructor
    public static class Properties {
        private String nickname;
        private String profile_image;

        public Properties(String nickname, String profile_image) {
            this.nickname = nickname;
            this.profile_image = profile_image;
        }
    }
}