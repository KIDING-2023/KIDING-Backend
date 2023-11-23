package com.demo.KIDING.controller;

import com.demo.KIDING.dto.SignUpReq;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.global.common.ValidErrorDetails;
import com.demo.KIDING.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.demo.KIDING.global.common.BaseResponseStatus.REQUEST_ERROR;
import static com.demo.KIDING.global.common.BaseResponseStatus.SUCCESS_TO_SIGNUP;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public BaseResponse signup(@RequestBody @Valid SignUpReq signUpReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ValidErrorDetails errorDetails = new ValidErrorDetails();
            return new BaseResponse<>(REQUEST_ERROR, errorDetails.validateHandler(bindingResult));

        }

        try {
            return new BaseResponse<>(userService.signup(signUpReq));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
