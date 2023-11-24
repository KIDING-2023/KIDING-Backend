package com.demo.KIDING.service;

import com.demo.KIDING.domain.BoardGame;
import com.demo.KIDING.dto.BoardGameRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.BoardGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;

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
}
