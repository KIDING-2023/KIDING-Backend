package com.demo.KIDING.global.auth;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByNickname(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // User 객체를 UserDetails 객체로 변환
    private UserDetails createUserDetails(User user) {
        // UserDetails를 Spring Security의 User 클래스로 생성
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getNickname())
                .password(user.getPassword()) // 비밀번호는 이미 인코딩된 상태여야 합니다.
                //.authorities(/* 권한 정보를 추가하세요. 예: Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) */)
                .authorities("ROLE_USER")
                .accountLocked(false) // 계정 잠금 여부 설정
                .disabled(false) // 계정 비활성화 여부 설정
                .build();
    }
}
