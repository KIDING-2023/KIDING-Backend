package com.demo.KIDING.global.jwt;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.auth0.jwt.algorithms.Algorithm;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.repository.UserRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;

//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    private final UserRepository userRepository;
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
//        super(authenticationManager);
//        this.userRepository = userRepository;
//    }
//
//    @Override  // 인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게 된다
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("인증이나 권한이 필요한 주소 요청됨");
//
//        String jwtHeader = request.getHeader("Authorization");
//        System.out.println("jwtHeader: " + jwtHeader);
//
//        // 헤더가 비어있거나 Bearer로 시작하지 않는다면 -> 문제가 있음
//        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")){
//            chain.doFilter(request, response);  // 다시 필터 타게끔
//            return;
//        }
//
//        // 받아온 jwt토큰을 검증해서 정상적인 사용자인지 확인
//        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
//
////        String username =
////                JWT.require(Algorithm.HMAC512("kiding")).build().verify(jwtToken).getClaim("nickname").asString();  // 유저 이름 가져오기
////        System.out.println(username);
//        Key secretKey = Keys.hmacShaKeyFor("kidingkidingkidingtestSecretKey20240815".getBytes());
//
//        // JWT 토큰 해독 및 클레임 추출
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(secretKey)  // 서명에 사용된 키 설정
//                .build()
//                .parseClaimsJws(jwtToken)  // 토큰을 해독 및 검증
//                .getBody();  // Claims 객체 가져오기
//
//        String nickname = claims.get("nickname", String.class);  // nickname 클레임 추출
//        Long userId = claims.get("id", Long.class);  // id 클레임 추출
//
//        if (nickname != null){  // 서명이 정상적으로 된 경우
//            User userEntity = userRepository.findByNickname(nickname).get();
//            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
//
//            // jwt토큰 서명을 통해 서명이 정상이면, authentication객체 만들어주기
//            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
//
//            // 시큐리티 세션에 접근해 authentication객체 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            chain.doFilter(request, response);
//        }
//    }
//}

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override  // 인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게 된다
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청됨");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: " + jwtHeader);

        // 헤더가 비어있거나 Bearer로 시작하지 않는다면 -> 문제가 있음
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request, response);  // 다시 필터 타게끔
            return;
        }

        // 받아온 jwt토큰을 검증해서 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        String username =
                JWT.require(Algorithm.HMAC512("dressmeup")).build().verify(jwtToken).getClaim("id").asString();  // 유저 이름 가져오기
        System.out.println(username);
        if (username != null){  // 서명이 정상적으로 된 경우
            User userEntity = userRepository.findByNickname(username).get();
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            // jwt토큰 서명을 통해 서명이 정상이면, authentication객체 만들어주기
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티 세션에 접근해 authentication객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}