package com.example.dlauth.api.dto;

import com.example.dlauth.domain.constant.MemberRole;
import com.example.dlauth.domain.constant.PlatformType;
import lombok.Builder;

@Builder
public record LoginPageResponse(
        PlatformType platformType,
        String url) {
}