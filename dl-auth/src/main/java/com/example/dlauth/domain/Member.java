package com.example.dlauth.domain;


import com.example.dlauth.domain.constant.LabRole;
import com.example.dlauth.domain.constant.MemberRole;
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

    @Embedded
    @Column(name = "PROFILE")
    private MemberProfile memberProfile;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "STUDENT_CODE", nullable = false, unique = true)
    private String studentCode;

    @Column(name = "DEPARTMENT", nullable = false)
    private String department;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "MEMBER_ROLE")
    @ColumnDefault(value = "'UNAUTH'")
    private MemberRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "LAB_ROLE")
    @ColumnDefault(value = "'LAB_NULL'")
    private LabRole labRole;

    @Builder
    public Member(Long labId, MemberProfile profile, String name, String studentCode, String department, MemberRole role, LabRole labRole) {
        this.labId = labId;
        this.memberProfile = profile;
        this.name = name;
        this.studentCode = studentCode;
        this.department = department;
        this.role = role;
        this.labRole = labRole;
    }

    // Spring Security UserDetails Area
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return studentCode;
    }

    @Override
    public String getUsername() {
        return studentCode;
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