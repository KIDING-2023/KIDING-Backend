package com.demo.KIDING.controller;

import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.dto.GamePlayReq;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RecentGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.service.BoardGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.demo.KIDING.global.common.BaseResponseStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardGameController {

    private final BoardGameService boardGameService;

    @GetMapping("/boardgames/main")
    public BaseResponse<List<BoardGameRes>> boardGamesMain() {

        try {
            List<BoardGameRes> boardGameResList = boardGameService.boardGamesMain();
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
    public BaseResponse<List<BoardGameRes>> boardGamesPopular() {
        try {
            List<BoardGameRes> boardGameResList = boardGameService.boardGamePopular();
            if (boardGameResList.isEmpty()) {
                return new BaseResponse<>(NO_BOARD_GAME_PLAYERS_YET);
            } else {
                return new BaseResponse<>(boardGameResList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/boardgame")
    public BaseResponse<List<RecentGameRes>> boardGameRecent(@RequestParam Long userId) {

        try {
            List<RecentGameRes> recentGames = boardGameService.boardGameRecent(userId);
            return new BaseResponse<>(recentGames);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/boardgame")  // 답변완료 api
    public BaseResponse<BaseResponseStatus> boardGamePlay(@RequestBody GamePlayReq gamePlayReq) {

        try {
            boardGameService.boardGamePlay(gamePlayReq.getName(), gamePlayReq.getUserId());
            return new BaseResponse<>(GAME_PLAYED);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/today/ranking")
    public BaseResponse<RankingRes> todayRanking() {

        try {
            return new BaseResponse<>(boardGameService.todayRanking());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
