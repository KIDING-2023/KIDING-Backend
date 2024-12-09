package com.demo.KIDING.service;

import com.demo.KIDING.domain.Friends;
import com.demo.KIDING.domain.User;
import com.demo.KIDING.dto.MyFriendRes;
import com.demo.KIDING.global.common.BaseException;
import com.demo.KIDING.repository.FriendsRepository;
import com.demo.KIDING.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.demo.KIDING.global.common.BaseResponseStatus.FRIEND_RELATION_NOT_FOUND;
import static com.demo.KIDING.global.common.BaseResponseStatus.NO_USER_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyFriendRes getFriends(Long userId, Long friendId) throws BaseException {
        if (!userRepository.existsById(userId)) {
            throw new BaseException(NO_USER_FOUND);
        }
        if (!userRepository.existsById(friendId)) {
            throw new BaseException(NO_USER_FOUND);
        }

        Optional<Friends> friendRelation = friendsRepository.findByFromUserIdAndToUserIdAndIsAcceptedTrue(userId, friendId);

        if (friendRelation.isEmpty()) {
            throw new BaseException(FRIEND_RELATION_NOT_FOUND);
        }

        User friend = friendRelation.get().getToUser();

        return MyFriendRes.builder()
                .nickname(friend.getNickname())
                .profile(friend.getProfile())
                .score(friend.getScore())
                .build();
    }
}
