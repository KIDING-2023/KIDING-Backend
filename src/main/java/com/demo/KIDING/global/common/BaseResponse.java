package com.demo.KIDING.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.demo.KIDING.global.common.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")  // 속성명 지정
    private final Boolean isSuccess;

    private final int code;

    private final String message;

    // null이 아니면 포함시킨다(응답으로 전달할 값들)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 요청에 성공한 경우
     */
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }

    /**
     * 요청에 실패한 경우
     */
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    /**
     * 형식적 Validation 응답
     */
    public BaseResponse(BaseResponseStatus status, T result) {
        this.isSuccess = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }
}
