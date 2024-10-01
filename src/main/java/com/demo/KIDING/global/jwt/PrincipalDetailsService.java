package com.demo.KIDING.global.jwt;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor  // login요청이 올때 동작
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername() 실행됨");
        User userEntity = userRepository.findByNickname(username).get();
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        System.out.println("userEntity : " + userEntity);
        System.out.println(username);

        return new PrincipalDetails(userEntity);
    }
}