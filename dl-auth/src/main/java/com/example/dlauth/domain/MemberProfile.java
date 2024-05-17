package com.example.dlauth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfile {

    @Column private String intro;
    @Column private String interests;
    @Column private String projects;

}
