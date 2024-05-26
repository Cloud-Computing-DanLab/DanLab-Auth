package com.example.dlauth.api.dto;

import com.example.dlauth.domain.constant.LabRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SignupRequest(
        Long labId,
        @NotBlank(message = "이름을 입력해주세요.")
        String name,
        @NotBlank(message = "학번을 입력해주세요.")
        String studentCode,
        @NotBlank(message = "학과를 입력해주세요.")
        String department,
        LabRole labRole
) {
}
