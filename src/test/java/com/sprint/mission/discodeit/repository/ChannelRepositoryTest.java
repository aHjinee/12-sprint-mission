package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager em;

    private Channel createChannel(ChannelType type, String name, String description) {
        return new Channel(type, name, description);
    }

    @Test
    @DisplayName("findAllByTypeOrIdIn 성공 - PUBLIC 타입이거나 id에 포함된 채널을 조회")
    void findAllByTypeOrIdIn_success() {
        Channel publicChannel = channelRepository.save(createChannel(ChannelType.PUBLIC, "공개채널", "공개 설명"));
        Channel privateChannel1 = channelRepository.save(createChannel(ChannelType.PRIVATE, null, null));
        Channel privateChannel2 = channelRepository.save(createChannel(ChannelType.PRIVATE, null, null));
        em.flush();
        em.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC, List.of(privateChannel1.getId()));

        assertThat(result)
                .extracting(Channel::getId)
                .containsExactlyInAnyOrder(publicChannel.getId(), privateChannel1.getId())
                .doesNotContain(privateChannel2.getId());
    }

    @Test
    @DisplayName("findAllByTypeOrIdIn 실패 - 조건에 맞는 채널이 없으면 빈 목록을 반환")
    void findAllByTypeOrIdIn_empty() {
        channelRepository.save(createChannel(ChannelType.PRIVATE, null, null));
        channelRepository.save(createChannel(ChannelType.PRIVATE, null, null));
        em.flush();
        em.clear();

        List<Channel> result = channelRepository.findAllByTypeOrIdIn(
                ChannelType.PUBLIC, List.of(UUID.randomUUID()));

        assertThat(result).isEmpty();
    }
}