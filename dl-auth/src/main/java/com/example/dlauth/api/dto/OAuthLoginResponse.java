package com.example.dlauth.api.dto;

import com.example.dlauth.domain.constant.PlatformType;
import lombok.Builder;

@Builder
public record OAuthLoginResponse (
        String platformId,
        PlatformType platformType
){
}
