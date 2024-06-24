package com.demo.KIDING.global.jwt;

import com.demo.KIDING.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static com.demo.KIDING.domain.Role.USER;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {


    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; //30분

    private long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; //일주일

    private final CustomUserDetailsService customUserDetailsService;
    public static final String HEADER_ACCESS_TOKEN = "X-ACCESS-TOKEN";
    public static final String HEADER_REFRESH_TOKEN = "X-REFRESH-TOKEN";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String nickname, String role) {
        Claims claims = Jwts.claims().setSubject(nickname);
        claims.put("role", "ROLE_" + role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByNickname(getUserNickname(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserNickname(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader(HEADER_ACCESS_TOKEN);
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(HEADER_REFRESH_TOKEN);
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
