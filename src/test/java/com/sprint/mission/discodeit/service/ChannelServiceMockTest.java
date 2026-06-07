package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChannelService 단위 테스트")
public class ChannelServiceMockTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService basicChannelService;

    private UUID channelId;
    private String name;
    private String description;

    private Channel publicChannel;
    private Channel privateChannel;
    private ChannelDto channelDto;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        name = "공지채널";
        description = "공지사항 채널입니다";

        publicChannel = new Channel(ChannelType.PUBLIC, name, description);
        privateChannel = new Channel(ChannelType.PRIVATE, null, null);
        channelDto = mock(ChannelDto.class);
    }

    @Test
    @DisplayName("public 채널 생성 성공")
    void createPublic_success() {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest(name, description);
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        ChannelDto result = basicChannelService.create(request);

        assertThat(result).isEqualTo(channelDto);
        then(channelRepository).should(times(1)).save(any(Channel.class));
        then(channelMapper).should(times(1)).toDto(any(Channel.class));
        then(readStatusRepository).should(never()).saveAll(anyList());
    }

    @Test
    @DisplayName("private 채널 생성 성공")
    void createPrivate_success() {
        List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);

        User user1 = new User("user1", "user1@email.com", "pw11112222", null);
        User user2 = new User("user2", "user2@email.com", "pw11112222", null);
        given(userRepository.findAllById(participantIds)).willReturn(List.of(user1, user2));
        given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

        ChannelDto result = basicChannelService.create(request);

        assertThat(result).isEqualTo(channelDto);
        then(channelRepository).should(times(1)).save(any(Channel.class));
        then(readStatusRepository).should(times(1)).saveAll(anyList());
        then(channelMapper).should(times(1)).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("findByUserId 성공")
    void findAllByUserId_success() {
        UUID userId = UUID.randomUUID();
        ReadStatus readStatus = new ReadStatus(
                new User("me", "me@email.com", "pw11112222", null),
                publicChannel,
                Instant.now());
        given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(readStatus));
        given(channelRepository.findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList()))
                .willReturn(List.of(publicChannel));
        given(channelMapper.toDto(publicChannel)).willReturn(channelDto);

        List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

        assertThat(result).containsExactly(channelDto);
        then(channelRepository).should(times(1))
                .findAllByTypeOrIdIn(eq(ChannelType.PUBLIC), anyList());
    }

    @Test
    @DisplayName("update 성공")
    void update_success() {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("새이름", "새설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        given(channelMapper.toDto(publicChannel)).willReturn(channelDto);

        ChannelDto result = basicChannelService.update(channelId, request);

        assertThat(result).isEqualTo(channelDto);
        then(channelRepository).should(times(1)).findById(channelId);
        then(channelMapper).should(times(1)).toDto(publicChannel);
    }

    @Test
    @DisplayName("update 실패 - 채널 없음")
    void update_fail_notFound() {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("새이름", "새설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> basicChannelService.update(channelId, request))
                .isInstanceOf(ChannelNotFoundException.class);

        then(channelMapper).should(never()).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("update 실패 - private 채널 수정 불가")
    void update_fail_privateChannel() {
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("새이름", "새설명");
        given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

        assertThatThrownBy(() -> basicChannelService.update(channelId, request))
                .isInstanceOf(PrivateChannelUpdateNotAllowedException.class);

        then(channelMapper).should(never()).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("delete 성공")
    void delete_success() {
        given(channelRepository.existsById(channelId)).willReturn(true);

        basicChannelService.delete(channelId);

        then(messageRepository).should(times(1)).deleteAllByChannelId(channelId);
        then(readStatusRepository).should(times(1)).deleteAllByChannelId(channelId);
        then(channelRepository).should(times(1)).deleteById(channelId);
    }

    @Test
    @DisplayName("delete 실패 - 채널 없음")
    void delete_fail_notFound() {
        given(channelRepository.existsById(channelId)).willReturn(false);

        assertThatThrownBy(() -> basicChannelService.delete(channelId))
                .isInstanceOf(ChannelNotFoundException.class);

        then(messageRepository).should(never()).deleteAllByChannelId(any());
        then(readStatusRepository).should(never()).deleteAllByChannelId(any());
        then(channelRepository).should(never()).deleteById(any());
    }
}