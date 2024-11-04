package com.demo.KIDING.controller;

import com.demo.KIDING.dto.SmsRequest;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

//    @PostMapping("/help/send")
//    public String getPhoneNumberForVerification(@RequestBody SmsRequest.PhoneNumberForVerificationRequest request) {
//        LocalDateTime sentAt = LocalDateTime.now();
//        smsService.sendVerificationMessage(request.getPhoneNumber(), sentAt);
//        return "인증번호를 보냈습니다.";
//    }

    @PostMapping("/help/send")
    public BaseResponse getPhoneNumberForVerification(@RequestBody SmsRequest.PhoneNumberForVerificationRequest request) {

        try {
            LocalDateTime sentAt = LocalDateTime.now();
            return new BaseResponse<>(smsService.sendVerificationMessage(request.getPhoneNumber(), sentAt));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }

    @PostMapping("/help/send/verify")
    public BaseResponse verificationByCode(@RequestBody SmsRequest.VerificationCodeRequest request) {

        try {
            LocalDateTime verifiedAt = LocalDateTime.now();
            return new BaseResponse<>(smsService.verifyCode(request.getCode(), verifiedAt));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }

    }
}
