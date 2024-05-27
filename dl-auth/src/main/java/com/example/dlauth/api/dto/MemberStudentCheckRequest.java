package com.example.dlauth.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberStudentCheckRequest(
        @NotBlank(message = "학번을 입력해주세요.")
        String studentCode
) {

}
