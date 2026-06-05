package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class UserStatusNotFoundException extends UserStatusException {
    public UserStatusNotFoundException() {
        super(ErrorCode.USER_STATUS_NOT_FOUND);
    }

    public static UserStatusNotFoundException withId(UUID id) {
        UserStatusNotFoundException ex = new UserStatusNotFoundException();
        ex.addDetail("UserStatusId", id);
        return ex;
    }

    public static UserStatusNotFoundException withUserId(UUID userId) {
        UserStatusNotFoundException ex = new UserStatusNotFoundException();
        ex.addDetail("userId", userId);
        return ex;
    }
}
