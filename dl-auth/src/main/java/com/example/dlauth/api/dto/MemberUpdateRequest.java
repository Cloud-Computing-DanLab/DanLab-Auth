package com.example.dlauth.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record MemberUpdateRequest(
//        @NotEmpty
//        MemberProfile memberProfile,

        @NotEmpty
        String name

) {

}
