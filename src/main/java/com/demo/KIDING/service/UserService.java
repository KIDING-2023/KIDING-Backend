package com.demo.KIDING.service;

import com.demo.KIDING.domain.*;
import com.demo.KIDING.dto.*;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.global.jwt.JwtToken;
import com.demo.KIDING.global.common.BaseException;
//import com.demo.KIDING.global.jwt.JwtTokenProvider;
import com.demo.KIDING.repository.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.demo.KIDING.domain.Role.ROLE_USER;
import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardGameRepository boardGameRepository;
    private final BookMarkRepository bookMarkRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RankingService rankingService;
    private final FriendsRepository friendsRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final GameUserRepository gameUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackFor = {Exception.class})
    public UserDtoRes signup(SignUpReq signUpReq) throws BaseException {


        entityManager.flush();  // 변경 내용을 DB에 반영
        entityManager.clear();  // JPA 1차 캐시 초기화

        if (userRepository.existsByNickname(signUpReq.getNickname())) {
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        if (userRepository.existsByPhone(signUpReq.getPhone())) {
            System.out.println("중복된 전화번호");
            throw new BaseException(POST_USERS_EXISTS_PHONE);
        }

        try {
            String encodedPwd = passwordEncoder.encode(signUpReq.getPassword());
            User newUser = userRepository.save(User.builder()
                    .nickname(signUpReq.getNickname())
                    .phone(signUpReq.getPhone())
                    .password(encodedPwd)
                    .activated(true)
                    .role(ROLE_USER)
                    .answers(0)
                    .score(0)
                    .players_with(0)
                    .kidingChip(0)
                    .build());
            log.info("닉네임 : " + newUser.getNickname() + " 이 회원가입을 완료했습니다." );

            return UserDtoRes.from(newUser);
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_SIGNUP);
        }
    }

    @Transactional
    public JwtToken signIn(String nickname, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(nickname, password);

        try {
            // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            JwtToken jwtToken = jwtProvider.generateToken(authentication);

            System.out.println("jwt: " + jwtToken);

            return jwtToken;
        } catch (BadCredentialsException e) {
            // 잘못된 자격 증명 처리
            System.err.println("Invalid credentials: " + e.getMessage());
            throw new RuntimeException("Invalid credentials provided"); // 사용자에게 적절한 메시지 반환
        } catch (AuthenticationException e) {
            // 인증 관련 다른 예외 처리
            System.err.println("Authentication error: " + e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

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

    // 북마크 설정
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

    // 북마크 해제
    @Transactional
    public void deleteBookmark(Long userId, Long boardgameId) throws BaseException {

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!boardGameRepository.existsById(boardgameId)) {
            throw new BaseException(NO_GAME_FOUND);
        }

        // 사용자가 즐겨찾기로 등록한 보드게임이 있는지 확인
        if (!bookMarkRepository.existsByUserIdAndBoardGameId(userId, boardgameId)) {
            throw new BaseException(BOOKMARK_NOT_FOUND);
        }

        // 즐겨찾기 삭제
        bookMarkRepository.deleteByUserIdAndBoardGameId(userId, boardgameId);

        log.info(userRepository.findById(userId).get().getNickname() + " 사용자가 `" + boardGameRepository.findById(boardgameId).get().getName() + "` 보드게임의 즐겨찾기를 해제했습니다.");
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

        // 현재 사용자 조회
        User loginUser = userRepository.findById(userId).get();
        int kidingChipCount = loginUser.getKidingChip();

        // 모든 사용자 목록 가져오기
        List<User> users = userRepository.findAll();

        int myRank = rankingService.calculateUserRanking(userId);

        // kidingChip 개수가 같은 사용자들의 닉네임 조회
        List<String> sameKidingChipNicknames = users.stream()
                .filter(user -> user.getKidingChip() == kidingChipCount)
                .map(User::getNickname)
                .collect(Collectors.toList());

        // 응답 생성
        return MyPageRes.builder()
                .nickname(loginUser.getNickname())
                .answers(loginUser.getAnswers())
                .score(loginUser.getScore())
                .players_with(loginUser.getPlayers_with())
                .kiding_chip(loginUser.getKidingChip())
                .rank(myRank)  // 현재 랭킹 추가
                .sameKidingChipNicknames(sameKidingChipNicknames)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SearchRes> searchItem(String word) throws BaseException {
        List<SearchRes> searchResList = new ArrayList<>();

        // 부분 검색을 위해 %를 추가
        String searchKeyword = "%" + word + "%";

        // 보드게임 이름으로 검색
        List<BoardGame> boardGames = boardGameRepository.searchByNameLike(searchKeyword);
        for (BoardGame game : boardGames) {
            SearchRes searchRes = SearchRes.builder()
                    .entityTypeValue(EntityType.BOARD_GAME.toString())
                    .id(game.getId())
                    .name(game.getName())
                    .build();
            searchResList.add(searchRes);
        }

        // 닉네임으로 검색
        List<User> users = userRepository.searchByUserNicknameLike(searchKeyword);
        for (User u : users) {
            SearchRes searchRes = SearchRes.builder()
                    .entityTypeValue(EntityType.USER.toString())
                    .id(u.getId())
                    .name(u.getNickname())
                    .image(u.getProfile())
                    .build();
            searchResList.add(searchRes);
        }

        if (searchResList.isEmpty()) {
            throw new BaseException(NO_DATA_FOUND);
        }
        return searchResList;
    }



    // 전화번호로 닉네임 찾기
    @Transactional(readOnly = true)
    public String findNickname(String phone) throws BaseException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new BaseException("사용자를 찾을 수 없습니다."));

        return user.getNickname();

    }

    // 비밀번호 재설정
    @Transactional
    public String resetPassword(String phoneNumber, String newPassword) throws BaseException {
        Optional<User> optionalUser = userRepository.findByPhone(phoneNumber);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get(); // Optional에서 User 객체 추출
            user.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 암호화

            userRepository.save(user);
            return "비밀번호가 재설정되었습니다.";
        } else {
            throw new BaseException("사용자를 찾을 수 없습니다.");
        }
    }

    // 닉네임 중복 확인
    public String checkNicknameDuplicate(String nickname) throws BaseException {

        String result = "사용 가능한 닉네임입니다.";
        if (userRepository.existsByNickname(nickname) == true) {
            result = "이미 존재하는 닉네임입니다.";
        }
        return result;
    }

    // 전화번호 중복 확인
    public String checkPhoneNumber(String phone) throws BaseException {

        String result = "사용 가능한 전화번호입니다.";
        if (userRepository.existsByPhone(phone) == true) {
            result = "이미 사용 중인 전화번호입니다.";
        }
        return result;
    }


    @Transactional // 회원 탈퇴 (user 데이터를 포함하여 친구 데이터도 전부 삭제)
    public String deleteUser(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 3️⃣ 친구 요청 삭제 (friend_request 테이블)
        friendRequestRepository.deleteAllByUserId(user);

        // 4️⃣ 친구 관계 삭제 (friends 테이블)
        friendsRepository.deleteAllByUserId(user);

        bookMarkRepository.deleteByUser(user);

        gameUserRepository.deleteByUser(user);

                userRepository.delete(user);
        return "회원 탈퇴가 완료되었습니다.";
    }

}
