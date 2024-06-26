package com.example.dlauth.domain;


import com.example.dlauth.domain.constant.LabRole;
import com.example.dlauth.domain.constant.MemberRole;
import com.example.dlauth.domain.constant.PlatformType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "LAB_ID")
    private Long labId;

    @Column(name = "INTRO")
    private String intro;

    @Column(name = "INTERESTS")
    private String interests;

    @Column(name = "PROJECTS")
    private String projects;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STUDENT_CODE")
    private String studentCode;

    @Column(name = "DEPARTMENT")
    private String department;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MEMBER_ROLE")
    @ColumnDefault(value = "'UNAUTH'")
    private MemberRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LAB_ROLE")
    @ColumnDefault(value = "'LAB_NULL'")
    private LabRole labRole;

    @Column(name = "PLATFORM_ID", nullable = false, unique = true)
    private String platformId;                                  // 플랫폼 아이디 (플랫폼 식별자)

    @Enumerated(EnumType.STRING)
    @Column(name = "PLATFORM_TYPE")
    @ColumnDefault(value = "'KAKAO'")
    private PlatformType platformType;

    @Builder
    public Member(Long labId, String intro, String interests, String projects, String name, String studentCode, String department, MemberRole role, LabRole labRole, String platformId, PlatformType platformType) {
        this.labId = labId;
        this.intro = intro;
        this.interests = interests;
        this.projects = projects;
        this.name = name;
        this.studentCode = studentCode;
        this.department = department;
        this.role = role;
        this.labRole = labRole;
        this.platformId = platformId;
        this.platformType = platformType;
    }

    // 회원가입 (UNAUTH -> MEMBER)
    public void signUp(String name, String studentCode, String department,
                       LabRole labRole, Long labId) {
        this.role = MemberRole.MEMBER;
        this.name = name;
        this.studentCode = studentCode;
        this.department = department;
        this.labRole = labRole;
        this.labId = labId;
    }

    // 회원 정보 수정 메서드
    public void updateMember(String name, String intro, String interests, String projects) {
        this.name = name;
        this.intro = intro;
        this.interests = interests;
        this.projects = projects;
    }

    // Spring Security UserDetails Area
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return platformId;
    }

    @Override
    public String getUsername() {
        return platformId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
