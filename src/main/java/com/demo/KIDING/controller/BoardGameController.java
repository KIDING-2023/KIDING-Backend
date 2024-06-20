package com.demo.KIDING.controller;

import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.dto.GamePlayReq;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RecentGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.global.common.BaseResponseStatus;
import com.demo.KIDING.service.BoardGameService;
import com.demo.KIDING.service.RankingService;
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
    private final RankingService rankingService;

    @GetMapping("/boardgames/{userId}/main")
    public BaseResponse<List<BoardGameRes>> boardGamesMain(@PathVariable Long userId) {

        try {
            List<BoardGameRes> boardGameResList = boardGameService.boardGamesMain(userId);
            if (boardGameResList.isEmpty()) {
                return new BaseResponse<>(NO_BOARD_GAME_YET);
            } else {
                return new BaseResponse<>(boardGameResList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("/boardgames/{userId}/popular")
    public BaseResponse<List<BoardGameRes>> boardGamesPopular(@PathVariable Long userId) {
        try {
            List<BoardGameRes> boardGameResList = boardGameService.boardGamePopular(userId);
            if (boardGameResList.isEmpty()) {
                return new BaseResponse<>(NO_BOARD_GAME_PLAYERS_YET);
            } else {
                return new BaseResponse<>(boardGameResList);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/boardgame/{userId}/recent")
    public BaseResponse<List<RecentGameRes>> boardGameRecent(@PathVariable Long userId) {

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

    @GetMapping("/today/ranking") // 랭킹 조회
    public BaseResponse<RankingRes> todayRanking() {

        try {
            rankingService.viewRanking();
            return new BaseResponse<>(boardGameService.todayRanking());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
