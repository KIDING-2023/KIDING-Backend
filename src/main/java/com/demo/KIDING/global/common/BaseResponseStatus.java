package com.demo.KIDING.global.common;

import lombok.Getter;

/*
200번대 : 성공적인 응답
400번대 : 클라이언트 응답 오류
500번대 : 서버 오류
 */
@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 200, "요청에 성공했습니다"),
    SUCCESS_TO_SIGNUP(true, 200, "회원가입에 성공했습니다"),
    FAILED_TO_SIGNUP(true, 400, "회원가입에 실패했습니다."),
    POST_USERS_EXISTS_NICKNAME(false, 400, "중복된 닉네임입니다."),
    REQUEST_ERROR(false, 400, "입력값을 확인해주세요."),
    NO_BOARD_GAME_YET(true, 200, "등록된 보드게임이 없습니다."),
    NO_BOARD_GAME_PLAYERS_YET(true, 200, "아직 게임 플레이어가 없습니다."),
    NO_GAME_PLAYED_YET(true, 200, "아직 보드게임에 참여하지 않았습니다."),
    NO_USER_FOUND(true, 200, "해당 유저는 존재하지 않습니다."),
    NO_GAME_FOUND(true, 200, "해당 게임은 존재하지 않습니다"),
    GAME_PLAYED(true, 200, "게임을 플레이하였습니다."),
    BOOKMARK_REQUESTED(true, 200, "즐겨찾기 설정이 완료되었습니다."),
    BOOKMARKED_ALREADY(true, 200, "이미 즐겨찾기가 설정된 게임입니다."),
    NO_BOOKMARK_YET(true, 200, "즐겨찾기 설정한 게임이 없습니다.");


    private final boolean isSuccess;

    private final int code;

    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
