package com.example.dlauth.fixture;


import com.example.dlauth.domain.Member;
import com.example.dlauth.domain.MemberProfile;
import com.example.dlauth.domain.constant.MemberRole;

public class MemberFixture {

    public static Member 비연구실_학생_생성() {
        return Member.builder()
                .name("이주성")
                .department("컴퓨터공학과")
                .role(MemberRole.MEMBER)
                .memberProfile(MemberProfile.builder()
                        .interests("자바 스프링 백엔드")
                        .intro("끈기의 사나이")
                        .projects("GITUDY 프로젝트 - 진행중\n " +
                                "BLUEROSE 프로젝트 - 진행중\n")
                        .build())
                .studentCode("32183520")
                .build();
    }
}
