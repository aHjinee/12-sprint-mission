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
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        boolean isAlreadyExists = data.stream()
                .anyMatch(msg -> msg.getId().equals(message.getId()));

        if (isAlreadyExists) {
            System.out.println("이미 존재하는 Id입니다.");
        } else {
            data.add(message);
        }
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return (Message) data.stream().filter(msg -> msg.getId().equals(messageId));
    }

    @Override
    public List<Message> findAll() {
         return data.stream().sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = find(messageId);
        message.update(newContent);
        return message;
    }

    @Override
    public void delete(UUID id) {
        Message message = find(id);
        data.removeIf(m -> m.getId().equals(id));
    }
}
