package com.demo.KIDING.controller;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.dto.BookMarkRes;
import com.demo.KIDING.dto.MyPageRes;
import com.demo.KIDING.dto.SignInReq;
import com.demo.KIDING.dto.SignUpReq;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.global.common.ValidErrorDetails;
import com.demo.KIDING.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SignInReq request) {
        try {
            String token = userService.login(request);
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @PostMapping("/character/{userId}/{num}")
    public BaseResponse character(@PathVariable Long userId, @PathVariable Integer num) {

        try {
            userService.character(userId, num);
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
}
