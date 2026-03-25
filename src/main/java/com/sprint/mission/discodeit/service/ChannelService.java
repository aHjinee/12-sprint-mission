package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {
    public abstract void save(Channel Channel);
    public abstract Channel findById(UUID id);
    public abstract List<Channel> findAll();
    public abstract Channel update(Channel Channel);
    public abstract Channel delete(UUID id);
}
