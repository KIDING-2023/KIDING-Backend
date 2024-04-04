package com.demo.KIDING.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RankingScheduler {

//    private final BoardGameService boardGameService;
//
//    @Scheduled(fixedDelay = 3000)  // 3초마다 실행
//    public void run() throws BaseException {
//        RankingRes rankingRes = boardGameService.todayRanking();
//        log.info("실행 갱신");
//    }

    private final RankingService rankingService;

    public RankingScheduler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    //
    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정에 랭킹 초기화
    public void resetRanking() {
        rankingService.resetRanking();
    }

}
