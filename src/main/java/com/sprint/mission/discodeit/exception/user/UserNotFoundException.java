package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    public static UserNotFoundException withUsername(String username){
        UserNotFoundException ex = new UserNotFoundException();
        ex.addDetail("username", username);
        return ex;
    }

    public static UserNotFoundException withId(UUID id){
        UserNotFoundException ex = new UserNotFoundException();
        ex.addDetail("userId", id);
        return ex;
    }

    public static UserNotFoundException withEmail(String email){
        UserNotFoundException ex = new UserNotFoundException();
        ex.addDetail("email", email);
        return ex;
    }

    public static UserNotFoundException withMessage(String message){
        UserNotFoundException ex = new UserNotFoundException();
        ex.addDetail("message", message);
        return ex;
    }
}
