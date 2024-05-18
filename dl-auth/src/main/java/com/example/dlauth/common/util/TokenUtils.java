package com.example.dlauth.common.util;

import com.example.dlauth.domain.Member;

import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
    public static Map<String, String> createTokenMap(Member member) {
        HashMap<String, String> map = new HashMap<>();

        map.put("role", String.valueOf(member.getRole()));
        map.put("name", String.valueOf(member.getName()));

        return map;
    }
}
