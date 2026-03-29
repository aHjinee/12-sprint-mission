package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void save(Message message);
    Optional<Message> findById(UUID id);
    List<Message> findBySenderIdAndRoomId(UUID senderId, UUID roomId);
    List<Message> findAll();
    Message update(MessageUpdateDto dto);
    Message delete(UUID id);

}
