package com.demo.KIDING.dto;

import lombok.Getter;

public class SmsRequest {
    @Getter
    public static class PhoneNumberForVerificationRequest{
        private String phoneNumber;
    }

    @Getter
    public static class VerificationCodeRequest{
        private String code;
    }
}
