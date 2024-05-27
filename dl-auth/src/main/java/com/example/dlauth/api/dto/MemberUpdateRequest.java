package com.example.dlauth.api.dto;

import com.example.dlauth.domain.MemberProfile;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record MemberUpdateRequest(
        @NotEmpty
        MemberProfile memberProfile,

        @NotEmpty
        String name

) {

}
