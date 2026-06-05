package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;

import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {
    public UserStatusAlreadyExistsException() {
        super(ErrorCode.DUPLICATE_USER_STATUS);
    }

    public static UserStatusAlreadyExistsException withUserid(UUID userid) {
        UserStatusAlreadyExistsException ex = new UserStatusAlreadyExistsException();
        ex.addDetail("userid", userid);
        return ex;
    }
}
