package com.demo.KIDING.service;

import com.demo.KIDING.domain.Ranking;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.RankingRes;
import com.demo.KIDING.dto.RankingResponse;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.RankingRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;


    @Transactional // 전체 랭킹 조회
    public List<RankingResponse> getRanking() {
        List<User> users = userRepository.findAll();

        // 사용자 목록을 칩 수 기준 내림차순 정렬
        List<User> sortedUsers = users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getKidingChip(), u1.getKidingChip()))
                .collect(Collectors.toList());

        if (!sortedUsers.isEmpty()) {
            // 1위 사용자 점수 업데이트
            User topUser = sortedUsers.get(0);
            topUser.updateScoreIfEligible();
            userRepository.save(topUser); // 변경사항 저장
        }

        // 정렬된 순서대로 RankingResponse 생성 및 순위 부여
        return IntStream.range(0, sortedUsers.size())
                .mapToObj(i -> {
                    User user = sortedUsers.get(i);
                    RankingResponse response = new RankingResponse(user.getNickname(), user.getKidingChip());
                    response.setRank(i + 1); // 정렬된 순서대로 순위 부여
                    return response;
                })
                .collect(Collectors.toList());
    }

    // Ranking 테이블에 저장하는 로직
//    @Transactional
//    public void updateRanking() {
//        // 기존 랭킹 초기화
//        rankingRepository.deleteAll();
//
//        // 사용자 목록을 칩 기준 내림차순 정렬
//        List<User> users = userRepository.findAll().stream()
//                .sorted((u1, u2) -> Integer.compare(u2.getKidingChip(), u1.getKidingChip()))
//                .collect(Collectors.toList());
//
//        // 새로운 랭킹 저장
//        IntStream.range(0, users.size()).forEach(i -> {
//            User user = users.get(i);
//            Ranking ranking = new Ranking();
//            ranking.setRankingPosition(i + 1); // 랭크는 1부터 시작
//            ranking.setUser(user);
//            ranking.setChips(user.getKidingChip());
//            System.out.println("Saved ranking: " + ranking.getRankingPosition() + " for user: " + user.getNickname());
//            rankingRepository.save(ranking);
//        });
//    }
//
//    @Transactional(readOnly = true)
//    public List<RankingResponse> getRanking() {
//        // 랭킹 테이블에서 데이터 가져오기
//        return rankingRepository.findAll().stream()
//                .map(r -> new RankingResponse(r.getUser().getNickname(), r.getChips(), r.getRankingPosition()))
//                .collect(Collectors.toList());
//    }

    // 초기화
    public void resetRanking() {
        rankingRepository.deleteAll();
    }

    // 랭킹에서 1위 추출
    public RankingRes getTopUserByAnswers() throws BaseException {

        User rankingUser = userRepository.findTopByOrderByAnswersDesc();
        return RankingRes.from(rankingUser);
    }

    /**
     * 특정 사용자의 랭킹을 계산
     * @param userId 사용자 ID
     * @return 사용자 랭킹
     */
    @Transactional(readOnly = true)
    public int calculateUserRanking(Long userId) {
        // 모든 사용자 가져오기
        List<User> users = userRepository.findAll();

        // kidingChip 기준 내림차순 정렬
        List<User> sortedUsers = users.stream()
                .sorted((u1, u2) -> Integer.compare(u2.getKidingChip(), u1.getKidingChip()))
                .collect(Collectors.toList());

        // 사용자 ID와 랭킹 매핑
        Map<Long, Integer> userRankMap = IntStream.range(0, sortedUsers.size())
                .boxed()
                .collect(Collectors.toMap(i -> sortedUsers.get(i).getId(), i -> i + 1));

        // 현재 사용자의 랭킹 반환
        return userRankMap.getOrDefault(userId, -1); // 랭킹 없으면 -1 반환
    }


}
