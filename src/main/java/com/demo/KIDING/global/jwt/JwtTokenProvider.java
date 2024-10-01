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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Service
public class JwtTokenProvider {


    //이메일 가져오기
    public String getNickname(String token) {
        return Jwts.parserBuilder()
                .setSigningKey("kiding")
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
