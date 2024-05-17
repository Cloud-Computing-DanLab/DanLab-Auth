package com.example.dlauth.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum LabRole {
    LAB_NULL("소속 연구실 없음"),
    LAB_MEMBER("연구실 소속원"),
    LAB_LEADER("연구실 리더");

    private final String role;
}