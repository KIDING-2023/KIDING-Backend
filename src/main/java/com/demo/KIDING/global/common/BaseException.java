package com.demo.KIDING.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseResponseStatus status;

    public BaseException(String message) {
        super(message);
    }

    // 에러 코드 정의
    public class ErrorCode {
        public static final String INVALID_TOKEN = "Invalid token";
        public static final String NO_USER_FOUND = "No user found";
    }
}

