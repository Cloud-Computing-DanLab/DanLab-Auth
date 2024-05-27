package com.example.dlauth.api.dto;

import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.MemberProfile;
import lombok.Builder;

@Builder
public record MemberUpdatePageResponse(
        MemberProfile memberProfile,
        String name
) {

    public static MemberUpdatePageResponse of(Member member) {
        return MemberUpdatePageResponse.builder()
                .memberProfile(member.getMemberProfile())
                .name(member.getName())
                .build();
    }
}
