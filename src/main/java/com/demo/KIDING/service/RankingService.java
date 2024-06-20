package com.demo.KIDING.service;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.RankingRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    // 초기화
    public void resetRanking() {
        rankingRepository.deleteAll();
    }

    // 랭킹 조회
    public void viewRanking() {
        rankingRepository.findAll();
    }

    public RankingRes getTopUserByAnswers() throws BaseException {

        User rankingUser = userRepository.findTopByOrderByAnswersAsc();
        return RankingRes.from(rankingUser);
    }


}
