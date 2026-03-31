package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    @Override
    public void save(Message message) {
        boolean isAlreadyExists = data.stream()
                .anyMatch(msg -> msg.getId().equals(message.getId()));

        if (isAlreadyExists) {
            System.out.println("이미 존재하는 Id입니다.");
        } else {
            data.add(message);
        }
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return data.stream().filter(msg -> msg.getId().equals(id)).findAny();
    }

    @Override
    public List<Message> findBySenderIdAndRoomId(UUID senderId, UUID roomId) {
        return data.stream().filter(
                msg ->msg.getSenderId().equals(senderId) && msg.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
         return data.stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Message update(MessageUpdateDto dto) { //dto에서 MessageId랑 내용만 받는 게 좋을까
        Message message = findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
        message.update(dto.content());
        return message;
    }

    @Override
    public Message delete(UUID id) {
        Message message = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
        data.removeIf(m -> m.getId().equals(id));
        return message;
    }
}
