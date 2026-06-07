package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager em;

    private User createAuthor(String username, String email) {
        User user =  userRepository.save(new User(username, email, "password1234", null));
        userStatusRepository.save(new UserStatus(user, Instant.now()));

        return user;
    }

    private Channel createChannel() {
        return channelRepository.save(new Channel(ChannelType.PUBLIC, "채널1", "채널1입니다."));
    }

    private Message createMessage(String content, Channel channel, User author) {
        return messageRepository.save(new Message(content, channel, author, List.of()));
    }

    @Test
    @DisplayName("findAllByChannelIdWithAuthor 성공 - 다음 슬라이스가 존재")
    void findAllByChannelIdWithAuthor_paging_success() {
        Channel channel = createChannel();
        User author = createAuthor("test01", "test01@email.com");
        createMessage("메시지1", channel, author);
        createMessage("메시지2", channel, author);
        createMessage("메시지3", channel, author);
        em.flush();
        em.clear();

        Instant createAt = Instant.now().plusSeconds(60);;
        Pageable pageable = PageRequest.of(0, 2, Sort.by("createdAt").descending());

        Slice<Message> slice =
                messageRepository.findAllByChannelIdWithAuthor(channel.getId(), createAt, pageable);

        assertThat(slice.getContent()).hasSize(2);
        assertThat(slice.hasNext()).isTrue();
    }

    @Test
    @DisplayName("findAllByChannelIdWithAuthor 실패 - 커서 이전 메시지가 없으면 빈 슬라이스를 반환")
    void findAllByChannelIdWithAuthor_empty() {
        Channel channel = createChannel();
        User author = createAuthor("test01", "test01@email.com");
        createMessage("메시지1", channel, author);
        em.flush();
        em.clear();

        Instant createAt = Instant.now().minusSeconds(60);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        Slice<Message> slice =
                messageRepository.findAllByChannelIdWithAuthor(channel.getId(), createAt, pageable);

        assertThat(slice.getContent()).isEmpty();
        assertThat(slice.hasNext()).isFalse();
    }

    @Test
    @DisplayName("findLastMessageAtByChannelId 성공 - 마지막 메시지 시각을 반환")
    void findLastMessageAtByChannelId_success() {
        Channel channel = createChannel();
        User author = createAuthor("test01", "test01@email.com");
        createMessage("메시지1", channel, author);
        createMessage("메시지2", channel, author);
        em.flush();
        em.clear();

        Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("findLastMessageAtByChannelId 실패 - 메시지가 없으면 빈 Optional을 반환")
    void findLastMessageAtByChannelId_empty() {
        Channel channel = createChannel();
        em.flush();
        em.clear();

        Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("deleteAllByChannelId 성공 - 채널의 모든 메시지를 삭제")
    void deleteAllByChannelId_success() {
        Channel channel = createChannel();
        User author = createAuthor("test01", "test01@email.com");
        createMessage("메시지1", channel, author);
        createMessage("메시지2", channel, author);
        em.flush();
        em.clear();

        messageRepository.deleteAllByChannelId(channel.getId());
        em.flush();
        em.clear();

        Instant cursor = Instant.now().plusSeconds(60);
        Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(
                channel.getId(), cursor, PageRequest.of(0, 10));

        assertThat(slice.getContent()).isEmpty();
    }
}