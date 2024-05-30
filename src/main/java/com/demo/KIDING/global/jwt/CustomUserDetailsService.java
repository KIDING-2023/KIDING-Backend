package com.demo.KIDING.global.jwt;

import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;


    public UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }
}
