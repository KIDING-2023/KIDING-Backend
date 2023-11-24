package com.demo.KIDING.controller;

import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.global.common.BaseResponse;
import com.demo.KIDING.service.BoardGameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.demo.KIDING.global.common.BaseResponseStatus.NO_BOARD_GAME_YET;

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
}
