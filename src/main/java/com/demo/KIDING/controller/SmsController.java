package com.demo.KIDING.controller;

import com.demo.KIDING.dto.SmsRequest;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/help/send")
    public String getPhoneNumberForVerification(@RequestBody SmsRequest.PhoneNumberForVerificationRequest request) {
        LocalDateTime sentAt = LocalDateTime.now();
        smsService.sendVerificationMessage(request.getPhoneNumber(), sentAt);
        return "인증번호를 보냈습니다.";
    }

    @PostMapping("/help/send/verify")
    public String verificationByCode(@RequestBody SmsRequest.VerificationCodeRequest request) throws BaseException {
        LocalDateTime verifiedAt = LocalDateTime.now();
        smsService.verifyCode(request.getCode(), verifiedAt);
        return "정상 인증 되었습니다.";
    }
}
