package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    void save(Channel Channel);
    Channel findById(UUID id);
    List<Channel> findAll();
    Channel update(ChannelUpdateDto dto);
    Channel delete(UUID id);
}
