package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE("입력 값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    ACCESS_DENIED("접근이 거부되었습니다."),

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND("메세지를 찾을 수 없습니다."),
    BINARY_CONTENT_NOT_FOUND("첨부파일을 찾을 수 없습니다."),
    READ_STATUS_NOT_FOUND("존재하지 않는 ReadStatus입니다."),
    USER_STATUS_NOT_FOUND("존재하지 않는 UserStatus입니다."),
    FILE_ERROR("파일 처리 중 오류가 발생했습니다.");


    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}