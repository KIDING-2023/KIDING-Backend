package com.demo.KIDING.global.jwt;

public class JwtProperties {
    public static final String HEADER_STRING = "Authorization"; // JWT를 담을 헤더 이름
    public static final String TOKEN_PREFIX = "Bearer "; // JWT 앞에 붙는 접두사
    public static final long EXPIRATION_TIME = 86400000; // JWT 만료 시간 (예: 1일)
    public static final String SECRET = "your_secret_key"; // JWT 서명에 사용할 비밀 키

    // 추가적인 JWT 관련 설정이 필요하다면 여기에 추가
}
