package com.example.dlauth.api.service.oauth.factory;

import com.example.dlauth.api.service.oauth.factory.adapter.OAuthAdapter;
import com.example.dlauth.api.service.oauth.factory.builder.OAuthURLBuilder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthFactory {

    private OAuthURLBuilder oAuthURLBuilder;
    private OAuthAdapter oAuthAdapter;

    @Builder
    private OAuthFactory(OAuthURLBuilder oAuthURLBuilder, OAuthAdapter oAuthAdapter) {
        this.oAuthURLBuilder = oAuthURLBuilder;
        this.oAuthAdapter = oAuthAdapter;
    }
}
