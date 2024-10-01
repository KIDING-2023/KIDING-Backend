package com.demo.KIDING.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo.KIDING.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;

//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Override  // login요청시 실행
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            // json 데이터 파싱하기
//            ObjectMapper om = new ObjectMapper();
//            User user = om.readValue(request.getInputStream(), User.class);
//            System.out.println("JwtAuthenticationFilter - Attempting authentication for user: " + user.getNickname());
//
////            System.out.println("JwtAuthenticationFilter: " + user);
////            System.out.println(user.getUserId());
////            System.out.println(user.getPassword());
//
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getNickname(), user.getPassword());
//            Authentication authentication =
//                    authenticationManager.authenticate(authenticationToken);
//
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//            System.out.println("로그인 완료, 유저이름: " + principalDetails.getUser().getNickname());
//
//            return authentication;  // 세션에 저장된다
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        System.out.println("successfulAuthentication이 실행됨 : 인증 완료");
//
//        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
//
//        // JWT 라이브러리 이용
////        String jwtToken = JWT.create()
////                .withSubject("토큰 발급")
////                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME ))  // 만료시간(1000=1초)
////                .withClaim("id", principalDetails.getUser().getId())
////                .withClaim("nickname", principalDetails.getUser().getNickname())
////                .sign(Algorithm.HMAC512("kiding"));  // 내 서버만 아는 고유 시크릿키
//
//        // 서명에 사용할 Secret Key 생성
//        Key secretKey = Keys.hmacShaKeyFor("kidingkidingkidingtestSecretKey20240815".getBytes()); // 시크릿 키를 바이트 배열로 변환
//
//        String jwtToken = Jwts.builder()
//                .setSubject("토큰 발급")
//                .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME)) // 만료 시간 설정
//                .claim("id", principalDetails.getUser().getId())  // 클레임 설정
//                .claim("nickname", principalDetails.getUser().getNickname())  // 클레임 설정
//                .signWith(secretKey)  // 서명 설정
//                .compact();  // 토큰 생성
//
//
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);  // 헤더에 담겨 사용자에게 응답된다
//
//        // 응답 본문에 사용자의 nickname 추가
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write("{\"id\" : \"" + principalDetails.getUser().getId() + "\", \"nickname\" : \"" + principalDetails.getUser().getNickname() + "\"}");
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        // 인증 실패 로그 추가
//        System.out.println("JwtAuthenticationFilter - Authentication failed: " + failed.getMessage());
//
//        // 401 Unauthorized 에러 코드 반환
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Authentication failed: " + failed.getMessage());
//
//    }
//}

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override  // login요청시 실행
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // json 데이터 파싱하기
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("JwtAuthenticationFilter - Attempting authentication for user: " + user.getId());

//            System.out.println("JwtAuthenticationFilter: " + user);
//            System.out.println(user.getUserId());
//            System.out.println(user.getPassword());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 완료, 유저이름: " + principalDetails.getUser().getId());

            return authentication;  // 세션에 저장된다

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication이 실행됨 : 인증 완료");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // JWT 라이브러리 이용
        String jwtToken = JWT.create()
                .withSubject("토큰 발급")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME ))  // 만료시간(1000=1초)
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("nickname", principalDetails.getUser().getNickname())
                .sign(Algorithm.HMAC512("dressmeup"));  // 내 서버만 아는 고유 시크릿키
//        System.out.println(jwtToken);
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);  // 헤더에 담겨 사용자에게 응답된다

        // 응답 본문에 사용자의 nickname 추가
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"id\" : \"" + principalDetails.getUser().getId() + "\", \"nickname\" : \"" + principalDetails.getUser().getNickname() + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 인증 실패 로그 추가
        System.out.println("JwtAuthenticationFilter - Authentication failed: " + failed.getMessage());

        // 401 Unauthorized 에러 코드 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication failed: " + failed.getMessage());

    }
}