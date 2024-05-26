package com.example.dlauth.api.controller;

import com.example.dlauth.common.response.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public JsonResult<?> test() {
        return JsonResult.successOf("test!!");
    }

    @GetMapping("/auth/test")
    public JsonResult<?> authTest() {
        return JsonResult.successOf("test!!");
    }
}
