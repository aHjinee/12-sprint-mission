package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;

import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException() {
        super(ErrorCode.MESSAGE_NOT_FOUND);
    }

    public static MessageNotFoundException withId(UUID id){
        MessageNotFoundException ex = new MessageNotFoundException();
        ex.addDetail("messageId", id);
        return ex;
    }
}
