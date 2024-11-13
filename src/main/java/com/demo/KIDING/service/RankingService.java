package com.demo.KIDING.service;

import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RankingResponse;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.RankingRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RankingResponse> getRanking() {
        List<User> users = userRepository.findAll();
        return IntStream.range(0, users.size())
                .mapToObj(i -> {
                    User user = users.get(i);
                    RankingResponse response = new RankingResponse(user.getNickname(), user.getKiding_chip());
                    response.setRank(i + 1); // 랭크는 1부터 시작
                    return response;
                })
                .sorted((r1, r2) -> Integer.compare(r2.getChips(), r1.getChips())) // 내림차순 정렬
                .collect(Collectors.toList());
    }

    // 초기화
    public void resetRanking() {
        rankingRepository.deleteAll();
    }

    public RankingRes getTopUserByAnswers() throws BaseException {

        User rankingUser = userRepository.findTopByOrderByAnswersDesc();
        return RankingRes.from(rankingUser);
    }


}
