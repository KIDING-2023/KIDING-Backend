package com.demo.KIDING.service;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.domain.GameUser;
import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.dto.RecentGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.BoardGameRepository;
import com.demo.KIDING.repository.GameUserRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.demo.KIDING.global.common.BaseResponseStatus.NO_GAME_PLAYED_YET;
import static com.demo.KIDING.global.common.BaseResponseStatus.NO_USER_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final GameUserRepository gameUserRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BoardGameRes> boardGamesMain() throws BaseException {

        log.info("메인 보드게임을 조회하였습니다.");

        return boardGameRepository.findAll().stream()
                .map(BoardGameRes::from)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<BoardGameRes> boardGamePopular() throws BaseException {
        log.info("인기 보드게임을 조회하였습니다.");

        return boardGameRepository.findAll().stream()
                .map(BoardGameRes::from)
                .sorted(Comparator.comparing(BoardGameRes::getPlayers).reversed())  // 플레이어수 많은 게임순으로 리턴
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecentGameRes> boardGameRecent(Long userId) throws BaseException{
        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }

        if (gameUserRepository.existsByUserId(userId)) {  // 보드게임 하나라도 해본 경우

            List<GameUser> byUserId = gameUserRepository.findByUserId(userId);
            List<RecentGameRes> boardGameResList = new ArrayList<>();
            for (GameUser gameUser : byUserId) {
                BoardGame bg = boardGameRepository.findById(gameUser.getBoardGame().getId()).get();
                boardGameResList.add(RecentGameRes.builder()
                        .name(bg.getName())
                        .players(bg.getPlayers())
                        .time(gameUser.getCreatedDate()).build());
            }
            boardGameResList.sort(Comparator.comparing(RecentGameRes::getTime).reversed()); // 최근 플레이순으로 정렬

            return boardGameResList;

        } else {
            log.info(userId + "사용자는 아직 보드게임을 플레이하지 않았습니다.");
            throw new BaseException(NO_GAME_PLAYED_YET);  // 아직 게임 플레이X
        }
    }
}
