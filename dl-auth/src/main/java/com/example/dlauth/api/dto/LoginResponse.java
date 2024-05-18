package com.example.dlauth.api.dto;

import com.example.dlauth.domain.constant.MemberRole;
import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        MemberRole role
) {
}
