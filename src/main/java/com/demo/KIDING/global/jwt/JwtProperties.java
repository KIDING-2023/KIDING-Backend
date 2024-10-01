package com.demo.KIDING.global.jwt;

public interface JwtProperties {
    String SECRET = "kiding";
    int EXPIRATION_TIME = 60000 * 100;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}