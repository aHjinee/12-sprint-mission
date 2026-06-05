package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidCredentialsException extends UserException {
    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_USER_CREDENTIALS);
    }

    public static InvalidCredentialsException wrongPassword(String password) {
        InvalidCredentialsException e = new InvalidCredentialsException();
        e.addDetail("wrong password", password);
        return e;
    }
} 