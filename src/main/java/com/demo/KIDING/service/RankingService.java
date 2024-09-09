package com.demo.KIDING.service;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.RankingRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;


    // 키딩칩 개수로 전체 랭킹 조회
    @Transactional(readOnly = true)
    public List<String> getRanking() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getKiding_chip(), s1.getKiding_chip())) // 내림차순 정렬
                .map(user -> user.getNickname() + " " + user.getKiding_chip() + "번")
                .collect(Collectors.toList());
    }

    // 초기화
    public void resetRanking() {
        rankingRepository.deleteAll();
    }

    public RankingRes getTopUserByAnswers() throws BaseException {

        User rankingUser = userRepository.findTopByOrderByAnswersAsc();
        return RankingRes.from(rankingUser);
    }


}
