package com.demo.KIDING.service;

import com.demo.KIDING.repository.RankingRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    // 초기화
    public void resetRanking() {
        rankingRepository.deleteAll();
    }

    // 랭킹 조회
    public void viewRanking() {
        rankingRepository.findAll();
    }


}
