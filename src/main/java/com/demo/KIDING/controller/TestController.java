package com.demo.KIDING.controller;

import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    //private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/test")
    public String test(@RequestHeader(value = "Authorization") String token) {
        log.info(token);
        String role = jwtProvider.getUserInfo(token);
        log.info("role: " + role);
        return "성공";
    }

    @PostMapping("/tokenTest")
    public String token_test(@RequestHeader(value = "Authorization") String token) {
        log.info(token);
        String role = jwtProvider.getUserInfo(token);
        log.info(role);
        LocalDate now = LocalDate.now();
        log.info(now.toString());

        return "role: " + role;
//        return "success";
    }
}
