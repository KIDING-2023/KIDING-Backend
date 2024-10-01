package com.demo.KIDING.global.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 로그인 요청일 경우 필터를 통과
        if (httpRequest.getRequestURI().equals("/signin")) {
            chain.doFilter(request, response);
            return;
        }

        // 1. Request Header에서 JWT 토큰 추출
        System.out.println("=== doFilter ===");
        String token = resolveToken((HttpServletRequest) request);
        System.out.println("토근 확인====");
        System.out.println(token);
        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            System.out.println("토근 유효함");
            Authentication authentication = jwtProvider.getAuthentication(token);
            System.out.println(authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        System.out.println("resolveToken: " + bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            System.out.println("resolveToken 처리 후: " + bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }


}
