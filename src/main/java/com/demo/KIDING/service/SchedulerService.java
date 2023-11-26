package com.demo.KIDING.service;

import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.global.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerService {

//    private final BoardGameService boardGameService;
//
//    @Scheduled(fixedDelay = 3000)  // 3초마다 실행
//    public void run() throws BaseException {
//        RankingRes rankingRes = boardGameService.todayRanking();
//        log.info("실행 갱신");
//    }
}
