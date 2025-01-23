package com.demo.KIDING.service;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.domain.GameUser;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RecentGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.repository.BoardGameRepository;
import com.demo.KIDING.repository.BookMarkRepository;
import com.demo.KIDING.repository.GameUserRepository;
import com.demo.KIDING.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final GameUserRepository gameUserRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public List<BoardGameRes> boardGamesMain(Long userId) throws BaseException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NO_USER_FOUND));

        log.info("메인 보드게임을 조회하였습니다.");

        // 가져온 보드게임에 즐겨찾기 설정 여부 표시해줘야함!!
        // 1. 모든 보드게임 가져와서 BoardGameRes로 변환
        // 2. 유저 id로 유저가 즐겨찾기한 보드게임 이름 찾기
        // 3. 보드게임 이름 for문으로 BoardGameRes 리스트에서 이름이 일치하면 북마크 여부 True로
        List<BoardGameRes> boardGameRes = boardGameRepository.findAll().stream()
                .map(BoardGameRes::from)
                .collect(Collectors.toList());

//        Optional<List<BookMark>> bookMarkedGames = bookMarkRepository.findByUserId(userId);

        // 즐겨찾기한 게임이름 추출
        List<String> bookmarkedGameNames = user.getBookMarks().stream()
                .map(bookmark -> bookmark.getBoardGame().getName())
                .collect(Collectors.toList());

        // 즐겨찾기 여부 표시
        for (BoardGameRes bg: boardGameRes) {
            for (String bookmarkedGame: bookmarkedGameNames) {
                if (Objects.equals(bg.getName(), bookmarkedGame)) {  // 이름 일치하면
                    bg.setBookmarked(true);
                }
            }
        }

        return boardGameRes;
    }

    @Transactional(readOnly = true)
    public List<BoardGameRes> boardGamePopular(Long userId) throws BaseException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(NO_USER_FOUND));

        log.info("인기 보드게임을 조회하였습니다.");

        List<BoardGameRes> boardGameRes = boardGameRepository.findAll().stream()
                .map(BoardGameRes::from)
                .sorted(Comparator.comparing(BoardGameRes::getPlayers).reversed())  // 플레이어수 많은 게임순으로 리턴
                .collect(Collectors.toList());

        // 즐겨찾기한 게임이름 추출
        List<String> bookmarkedGameNames = user.getBookMarks().stream()
                .map(bookmark -> bookmark.getBoardGame().getName())
                .collect(Collectors.toList());

        // 즐겨찾기 여부 표시
        for (BoardGameRes bg: boardGameRes) {
            for (String bookmarkedGame: bookmarkedGameNames) {
                if (Objects.equals(bg.getName(), bookmarkedGame)) {  // 이름 일치하면
                    bg.setBookmarked(true);
                }
            }
        }

        return boardGameRes;
    }

    @Transactional(readOnly = true)
    public List<RecentGameRes> boardGameRecent(Long userId) throws BaseException {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }

        // 1. 그 유저가 플레이한 기록 GameUser에서 조회
        // 2. RecentGameRes 리스트 만들어서 북마크 여부 빼고 값 넣기
        // 3. user.getBookMarks() 로 즐겨찾기한 게임 이름 추출
        // 4. RecentGameRes에서 즐겨찾기한 이름 나오면 값 true로 변경
        if (gameUserRepository.existsByUserId(userId)) {  // 보드게임 하나라도 해본 경우
            User user = userRepository.findById(userId).get();
            List<GameUser> byUserId = gameUserRepository.findByUserId(userId);  // 게임 기록 조회
            Map<String, RecentGameRes> uniqueGames = new HashMap<>(); // 게임 이름 기준 중복 제거를 위한 Map

            for (GameUser gameUser : byUserId) {  // 보드게임 이름 가져와서 값 넣어주기
                BoardGame bg = boardGameRepository.findByName(gameUser.getBoardGame().getName()).get();
                String gameName = bg.getName();
                RecentGameRes recentGame = uniqueGames.get(gameName);

                // 현재 기록이 더 최신일 경우 업데이트
                if (recentGame == null || gameUser.getCreatedDate().compareTo(recentGame.getTime()) > 0) {
                    uniqueGames.put(gameName, RecentGameRes.builder()
                            .name(gameName)
                            .players(bg.getPlayers())
                            .bookmarked(false)
                            .time(gameUser.getCreatedDate()).build());
                }
            }

            // 즐겨찾기한 게임 이름 추출
            List<String> bookmarkedGameNames = user.getBookMarks().stream()
                    .map(bookmark -> bookmark.getBoardGame().getName())
                    .collect(Collectors.toList());

            // 즐겨찾기 여부 표시
            uniqueGames.values().forEach(recentGameRes -> {
                if (bookmarkedGameNames.contains(recentGameRes.getName())) {
                    recentGameRes.setBookmarked(true);
                }
            });

            // Map에서 값만 추출해 리스트로 변환 후 정렬
            List<RecentGameRes> boardGameResList = new ArrayList<>(uniqueGames.values());
            boardGameResList.sort(Comparator.comparing(RecentGameRes::getTime).reversed()); // 최근 플레이순으로 정렬

            return boardGameResList;

        } else {
            log.info(userId + " 사용자는 아직 보드게임을 플레이하지 않았습니다.");
            throw new BaseException(NO_GAME_PLAYED_YET);  // 아직 게임 플레이 X
        }
    }

    @Transactional
    public void boardGamePlay(Long boardgameId, String accessToken, int count) throws BaseException {
        // Access Token에서 사용자 정보 추출
        Claims claims = jwtProvider.parseClaims(accessToken);
        String username = claims.get("nickname", String.class);

        // 사용자 조회
        Optional<User> optionalUser = userRepository.findByNickname(username);
        if (optionalUser.isEmpty()) {
            throw new BaseException(NO_USER_FOUND);
        }
        User loginUser = optionalUser.get();

        // 보드게임 존재 여부 확인
        if (!boardGameRepository.existsById(boardgameId)) {
            throw new BaseException(NO_GAME_FOUND);
        }

        // 보드게임 및 사용자 데이터 가져오기
        BoardGame game = boardGameRepository.findById(boardgameId).get();

        // 게임 플레이 처리
        game.playGame();
        loginUser.playGame();

        // 사용자 키딩칩 업데이트
        loginUser.setKidingChip(loginUser.getKidingChip() + count);
        userRepository.save(loginUser); // 변경된 사용자 정보 저장
    }


    @Transactional
    public void boardGamePlayFinal(Long boardgameId, Long userId) throws BaseException{

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!boardGameRepository.existsById(boardgameId)) {
            throw new BaseException(NO_GAME_FOUND);
        }
        BoardGame game = boardGameRepository.findById(boardgameId).get();
        // gameUser에 업데이트, 키딩칩 & 누적 대답수 +1, boargame player +1
        gameUserRepository.save(GameUser.builder()
                .user(userRepository.findById(userId).get())
                .boardGame(boardGameRepository.findByName(game.getName()).get()).build());

        User loginUser = userRepository.findById(userId).get();
        BoardGame boardGame = boardGameRepository.findByName(game.getName()).get();
        boardGame.playGame();

        log.info(userId +"번 사용자가 " + game.getName() + " 보드게임을 플레이하였습니다.");
    }

    // 주사위 눈
    public int rollDice(Long boardgameId, Long userId, Long count) throws BaseException {

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!boardGameRepository.existsById(boardgameId)) {
            throw new BaseException(NO_GAME_FOUND);
        }

        if (count == 0) {
            return 1; // 플레이한 횟수가 0이면 항상 1 반환
        }

        // 주사위 눈을 랜덤으로 결정 (1부터 6까지)
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
