package com.demo.KIDING.controller;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.*;
import com.demo.KIDING.global.jwt.JwtProvider;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.repository.UserRepository;
import com.demo.KIDING.service.BoardGameService;
import com.demo.KIDING.service.RankingService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final RankingService rankingService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @GetMapping("/boardgames/main")
    public BaseResponse<List<BoardGameRes>> boardGamesMain(@RequestHeader(value = "Authorization") String accessToken) {

        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String username = claims.get("nickname", String.class);
            Optional<User> optionalUser = userRepository.findByNickname(username);
            List<BoardGameRes> boardGameResList = boardGameService.boardGamesMain(optionalUser.get().getId());
            if (boardGameResList.isEmpty()) {
                return new BaseResponse<>(NO_BOARD_GAME_YET);
            } else {
                return new BaseResponse<>(boardGameResList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/boardgames/popular")
    public BaseResponse<List<BoardGameRes>> boardGamesPopular(@RequestHeader(value = "Authorization") String accessToken) {
        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String username = claims.get("nickname", String.class);
            Optional<User> optionalUser = userRepository.findByNickname(username);
            List<BoardGameRes> boardGameResList = boardGameService.boardGamePopular(optionalUser.get().getId());
            if (boardGameResList.isEmpty()) {
                return new BaseResponse<>(NO_BOARD_GAME_PLAYERS_YET);
            } else {
                return new BaseResponse<>(boardGameResList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/boardgames/recent")
    public BaseResponse<List<RecentGameRes>> boardGameRecent(@RequestHeader(value = "Authorization") String accessToken) {

        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String username = claims.get("nickname", String.class);
            Optional<User> optionalUser = userRepository.findByNickname(username);
            List<RecentGameRes> recentGames = boardGameService.boardGameRecent(optionalUser.get().getId());
            return new BaseResponse<>(recentGames);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/boardgame") // 답변완료 api
    public BaseResponse<BaseResponseStatus> boardGamePlay(
            @RequestHeader(value = "Authorization") String accessToken,
            @RequestBody GamePlayReq gamePlayReq) {
        try {

            // GamePlayReq에서 boardGameId와 count 추출
            Long boardGameId = gamePlayReq.getBoardGameId();
            int count = gamePlayReq.getCount();
            // 서비스 호출 (accessToken과 boardGameId 전달)
            boardGameService.boardGamePlay(gamePlayReq.getBoardGameId(), accessToken, count);

            return new BaseResponse<>(GAME_PLAYED);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }



    @PostMapping("/boardgame/final")  // 최종 답변
    public BaseResponse<BaseResponseStatus> boardGamePlayFinal(@RequestHeader(value = "Authorization") String accessToken, @RequestBody GamePlayReq gamePlayReq) {

        try {
            Claims claims = jwtProvider.parseClaims(accessToken);
            String username = claims.get("nickname", String.class);
            Optional<User> optionalUser = userRepository.findByNickname(username);
            boardGameService.boardGamePlayFinal(gamePlayReq.getBoardGameId(), optionalUser.get().getId());
            return new BaseResponse<>(GAME_PLAYED);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/ranking/today") // 오늘의 랭킹 조회
    public BaseResponse<RankingRes> todayRanking() {

        try {
            // userRepository에서 게임플레이 횟수 오름차순으로 사용자 한명 정보만 반환
            return new BaseResponse<>(rankingService.getTopUserByAnswers());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/ranking/all") // 전체 랭킹 조회
    public List<RankingResponse> viewRanking() {
        return rankingService.getRanking();
    }

    @GetMapping("/boardgame/rollDice")
    public BaseResponse<RankingRes> rollDice(@RequestBody Long count) {

        try {
            // userRepository에서 게임플레이 횟수 오름차순으로 사용자 한명 정보만 반환
            return new BaseResponse<>(rankingService.getTopUserByAnswers());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
