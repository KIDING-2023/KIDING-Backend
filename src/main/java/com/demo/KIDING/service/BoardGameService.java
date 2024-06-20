package com.demo.KIDING.service;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.domain.BookMark;
import com.demo.KIDING.domain.GameUser;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.dto.BookMarkRes;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RecentGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.BoardGameRepository;
import com.demo.KIDING.repository.BookMarkRepository;
import com.demo.KIDING.repository.GameUserRepository;
import com.demo.KIDING.repository.UserRepository;
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
    private final BookMarkRepository bookMarkRepository;


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
    public List<RecentGameRes> boardGameRecent(Long userId) throws BaseException{
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
            List<RecentGameRes> boardGameResList = new ArrayList<>();
            for (GameUser gameUser : byUserId) {  // 보드게임 이름 가져와서 값 넣어주기
                BoardGame bg = boardGameRepository.findByName(gameUser.getBoardGame().getName()).get();
                boardGameResList.add(RecentGameRes.builder()
                        .name(bg.getName())
                        .players(bg.getPlayers())
                        .bookmarked(false)
                        .time(gameUser.getCreatedDate()).build());
            }

            // 즐겨찾기한 게임이름 추출
            List<String> bookmarkedGameNames = user.getBookMarks().stream()
                    .map(bookmark -> bookmark.getBoardGame().getName())
                    .collect(Collectors.toList());

            // 즐겨찾기 여부 표시
            for (RecentGameRes recentGameRes: boardGameResList) {
                for (String bookmarkedGame: bookmarkedGameNames) {
                    if (Objects.equals(recentGameRes.getName(), bookmarkedGame)) {  // 이름 일치하면
                        recentGameRes.setBookmarked(true);
                    }
                }
            }

            boardGameResList.sort(Comparator.comparing(RecentGameRes::getTime).reversed()); // 최근 플레이순으로 정렬

            return boardGameResList;

        } else {
            log.info(userId + "사용자는 아직 보드게임을 플레이하지 않았습니다.");
            throw new BaseException(NO_GAME_PLAYED_YET);  // 아직 게임 플레이 X
        }
    }

    @Transactional
    public void boardGamePlay(String gameName, Long userId) throws BaseException{
//        log.info(userId.getClass().getName());
//        log.info(String.valueOf(userId));

        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!boardGameRepository.existsByName(gameName)) {
            throw new BaseException(NO_GAME_FOUND);
        }

        // gameUser에 업데이트, 키딩칩 & 누적 대답수 +1, boargame player +1
        gameUserRepository.save(GameUser.builder()
                .user(userRepository.findById(userId).get())
                .boardGame(boardGameRepository.findByName(gameName).get()).build());

        User loginUser = userRepository.findById(userId).get();
        BoardGame boardGame = boardGameRepository.findByName(gameName).get();
        boardGame.playGame();
        loginUser.playGame();

        log.info(userId +" 사용자가 " + gameName + " 보드게임을 플레이하였습니다.");
    }

    @Transactional(readOnly = true)
    public RankingRes todayRanking() throws BaseException {

        Optional<List<RankingRes>> rankingRes = userRepository.findRankUser();

        if (rankingRes.get().isEmpty()) {
            throw new BaseException(NO_BOARD_GAME_PLAYERS_YET);
        }

        return rankingRes.get().get(0);

    }
}
