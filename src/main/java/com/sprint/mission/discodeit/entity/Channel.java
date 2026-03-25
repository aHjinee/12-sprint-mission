package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private String channelName;
    private String channelDescription;
    private ChannelType type;
    private boolean isPrivate;
    private Long createdAt;
    private Long updatedAt;

    public Channel(String channelName, String channelDescription, ChannelType type, boolean isPrivate) {
        id = UUID.randomUUID();
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.type = type;
        this.isPrivate = isPrivate;
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public ChannelType getType() { return type; }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String channelName, String channelDescription, ChannelType type, boolean isPrivate){
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.type = type;
        this.isPrivate = isPrivate;
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", channelDescription='" + channelDescription + '\'' +
                ", type=" + type +
                ", isPrivate=" + isPrivate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
