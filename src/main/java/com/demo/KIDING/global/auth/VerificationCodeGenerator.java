package com.demo.KIDING.global.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationCodeGenerator {

    private static final Integer EXPIRATION_TIME_IN_MINUTES = 5;
    private static final SecureRandom random = new SecureRandom();

    public static VerificationCode generateVerificationCode(LocalDateTime sentAt) {
        //String code = UUID.randomUUID().toString();
        String code = generateRandomCode();
        return VerificationCode.builder()
                .code(code)
                .createAt(sentAt)
                .expirationTimeInMinutes(EXPIRATION_TIME_IN_MINUTES)
                .build();
    }
    private static String generateRandomCode() {
        // 6자리 난수 생성
        int codeNumber = random.nextInt(1000000); // 0부터 999999까지의 난수 생성
        return String.format("%06d", codeNumber); // 6자리로 포맷
    }
}
