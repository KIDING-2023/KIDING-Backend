package com.demo.KIDING.service;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.domain.BookMark;
import com.demo.KIDING.domain.Role;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.*;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.jwt.JwtTokenProvider;
import com.demo.KIDING.repository.BoardGameRepository;
import com.demo.KIDING.repository.BookMarkRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;
    private final BookMarkRepository bookMarkRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = {Exception.class})
    public UserDtoRes signup(SignUpReq signUpReq) throws BaseException {

        if (userRepository.existsByNickname(signUpReq.getNickname())) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        if (userRepository.existsByPhone(signUpReq.getPhone())) {
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        try {
            String encodedPwd = passwordEncoder.encode(signUpReq.getPassword());
            User newUser = userRepository.save(User.builder()
                    .nickname(signUpReq.getNickname())
                    .phone(signUpReq.getPhone())
                    .password(encodedPwd)
                    .activated(true)
                    .role(Role.USER)
                    .answers(0)
                    .score(0)
                    .players_with(0)
                    .kiding_chip(0)
                    .build());
            log.info("닉네임 : " + newUser.getNickname() + " 이 회원가입을 완료했습니다." );

            return UserDtoRes.from(newUser);
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_SIGNUP);
        }
    }

    @Transactional
    public String login(SignInReq request) {
        User user = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("가입된 닉네임이 아닙니다."));
//        validateMatchedPassword(request.getPassword(), user.getPassword());

        String role = user.getRole().name();
        return jwtTokenProvider.createToken(user.getNickname(), role);
    }

//    private void validateMatchedPassword(String rawPassword, String encodedPassword) {
//        // 비밀번호 검증 로직
//        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//        }
//    }

    @Transactional
    public void character(Long userId, Integer num) throws BaseException {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        User loginUser = userRepository.findById(userId).get();
        log.info("유저 찾음");
        loginUser.setCharacter(num);
        log.info("캐릭터 설정을 완료하였습니다.");

    }

    @Transactional
    public void bookmark(Long userId, Long boardgameId) throws BaseException{

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!boardGameRepository.existsById(boardgameId)) {
            throw new BaseException(NO_GAME_FOUND);
        }

        if (bookMarkRepository.existsByUserIdAndBoardGameId(userId, boardgameId)) {
            throw new BaseException(BOOKMARKED_ALREADY);
        }
        Optional<User> userById = userRepository.findById(userId);
        Optional<BoardGame> gameById = boardGameRepository.findById(boardgameId);

        BookMark bookMark = bookMarkRepository.save(BookMark.builder()
                .user(userById.get())
                .boardGame(gameById.get())
                .build());

        log.info(userById.get().getNickname() + " 사용자가 `" + gameById.get().getName() + "` 보드게임을 즐겨찾기 설정했습니다.");
        
    }
    @Transactional(readOnly = true)
    public List<BookMarkRes> getAllBookMark(Long userId) throws BaseException {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }


        Optional<List<BookMark>> byUserId = bookMarkRepository.findByUserId(userId);
        if (byUserId.get().isEmpty()) {
            throw new BaseException(NO_BOOKMARK_YET);
        }
        List<BookMarkRes> bookMarkResList = new ArrayList<>();

        for (BookMark bookMark : byUserId.get()) {
            BookMarkRes bookMarkRes = BookMarkRes.builder()
                    .BoardGameId(bookMark.getId())
                    .name(bookMark.getBoardGame().getName())
                    .players(bookMark.getBoardGame().getPlayers())
                    .build();
            bookMarkResList.add(bookMarkRes);
        }

        return bookMarkResList;

    }

    @Transactional(readOnly = true)
    public MyPageRes getMyPage(Long userId) throws BaseException {

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }

        User loginUser = userRepository.findById(userId).get();
        return MyPageRes.builder()
                .nickname(loginUser.getNickname())
                .answers(loginUser.getAnswers())
                .score(loginUser.getScore())
                .players_with(loginUser.getPlayers_with())
                .kiding_chip(loginUser.getKiding_chip()).build();
    }
}
