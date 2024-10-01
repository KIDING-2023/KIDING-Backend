package com.demo.KIDING.controller;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.*;
import com.demo.KIDING.global.auth.JwtProvider;
import com.demo.KIDING.global.auth.JwtToken;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.ValidErrorDetails;
import com.demo.KIDING.repository.UserRepository;
import com.demo.KIDING.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;
import static org.ietf.jgss.GSSException.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    //private final JwtTokenProvider jwtTokenProvider;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public BaseResponse signup(@RequestBody @Valid SignUpReq signUpReq, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ValidErrorDetails errorDetails = new ValidErrorDetails();
            return new BaseResponse<>(REQUEST_ERROR, errorDetails.validateHandler(bindingResult));
        }
        try {
            return new BaseResponse<>(userService.signup(signUpReq));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/signin")
    public JwtToken signIn(@RequestBody SignInReq request) {
        String nickname = request.getNickname();
        String password = request.getPassword();
        JwtToken jwtToken = userService.signIn(nickname, password);
        log.info("request username = {}, password = {}", nickname, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/character/{num}")
    public BaseResponse character(@PathVariable Integer num, @RequestHeader(value = "Authorization") String accessToken) {
        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String username = claims.get("nickname", String.class);
            Optional<User> optionalUser = userRepository.findByNickname(username);
            userService.character(optionalUser.get().getId(), num);
            return new BaseResponse<>(SUCCESS_TO_CHARACTER);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/bookmark/{userId}/{boardgameId}")
    public BaseResponse bookmark(@PathVariable Long userId, @PathVariable Long boardgameId) {

        try {
            userService.bookmark(userId, boardgameId);
            return new BaseResponse<>(BOOKMARK_REQUESTED);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/bookmark/{userId}")
    public BaseResponse<List<BookMarkRes>> getAllBookMark(@PathVariable Long userId) {

        try {
            return new BaseResponse(userService.getAllBookMark(userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{userId}/mypage")
    public BaseResponse<MyPageRes> getMyPage(@PathVariable Long userId) {

        try {
            return new BaseResponse<>(userService.getMyPage(userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/search")
    public BaseResponse<List<SearchRes>> searchItem(@RequestHeader(value = "Authorization") String token, @RequestParam String word) {
        log.info(token);
        String role = jwtProvider.getUserInfo(token);
        log.info(role);
        LocalDate now = LocalDate.now();
        log.info(now.toString());
        try {
            return new BaseResponse<>(userService.searchItem(word));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 친구 리스트 확인
    @GetMapping("/{userId}/friends")
    public BaseResponse<List<MyFriendRes>> friendsList(@PathVariable Long userId) {
        try {
            return new BaseResponse<>(userService.getFriendsList(userId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/help/findNickname")
    public BaseResponse findNickname(@RequestParam(value = "phone") String phone) {
        try {
            return new BaseResponse<>(userService.findNickname(phone));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }

    // 추후 삭제 예정
    @GetMapping("/help/findPassword")
    public BaseResponse findPassword(@RequestParam(value = "phone") String phone) {
        try {
            return new BaseResponse<>(userService.findPassword(phone));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }
}
