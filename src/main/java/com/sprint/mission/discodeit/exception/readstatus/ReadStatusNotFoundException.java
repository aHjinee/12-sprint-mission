package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;

import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {
    public ReadStatusNotFoundException() {
        super(ErrorCode.READ_STATUS_NOT_FOUND);
    }
    public static ReadStatusNotFoundException withId(UUID id){
        ReadStatusNotFoundException ex = new ReadStatusNotFoundException();
        ex.addDetail("readStatusId", id);
        return ex;
    }
}
