package com.example.dlauth.api.dto;

import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.MemberProfile;
import com.example.dlauth.domain.constant.LabRole;
import com.example.dlauth.domain.constant.MemberRole;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
        Long memberId,
        MemberRole role,
        Long labId,
        LabRole labRole,
        String name,
        MemberProfile memberProfile,
        String studentCode,
        String department
) {
    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getRole(),
                member.getLabId(),
                member.getLabRole(),
                member.getName(),
                member.getMemberProfile(),
                member.getStudentCode(),
                member.getDepartment()
        );
    }
}
