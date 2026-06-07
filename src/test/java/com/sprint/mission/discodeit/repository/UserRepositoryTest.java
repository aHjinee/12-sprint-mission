package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private TestEntityManager em;

    private User createUser(String username, String email, BinaryContent profile) {
        return new User(username, email, "password123", profile);
    }

    @Test
    @DisplayName("username으로 사용자 조회")
    public void findByUsername_success() {
        //given
        userRepository.save(createUser("user1",
                "user1@email.com", null));
        em.flush();
        em.clear();

        //when
        Optional<User> user = userRepository.findByUsername("user1");

        //then
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo("user1");
        assertThat(user.get().getEmail()).isEqualTo("user1@email.com");
    }

    @Test
    @DisplayName("username으로 없는 사용자 조회")
    public void findByUsername_notfound() {
        assertThat(userRepository.findByUsername("no_user")).isEmpty();
    }

    @Test
    @DisplayName("email 중복 여부 확인")
    public void existsByEmail() {
        // given
        userRepository.save(createUser("user2", "user2@example.com", null));
        em.flush();
        em.clear();

        // then
        assertThat(userRepository.existsByEmail("user2@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("none@example.com")).isFalse();
    }

    @Test
    @DisplayName("email 중복 여부 확인")
    public void existsByUsername() {
        // given
        userRepository.save(createUser("user3", "user3@example.com", null));
        em.flush();
        em.clear();

        // then
        assertThat(userRepository.existsByUsername("user3")).isTrue();
        assertThat(userRepository.existsByUsername("no_user")).isFalse();
    }

    @Test
    @DisplayName("전체조회 - 프로필과 상태 함께 조회 ")
    public void findAllWithProfileAndStatus() {

        //given
        BinaryContent binaryContent1 = new BinaryContent("a.png", 1L, "png");
        User user4 = userRepository.save(createUser("user4", "user4@example.com", binaryContent1));
        userStatusRepository.save(new UserStatus(user4, Instant.parse("2026-06-07T12:00:00Z")));

        BinaryContent binaryContent2 = new BinaryContent("a.png", 1L, "png");
        User user5 = userRepository.save(createUser("user5", "user5@example.com", binaryContent2));
        userStatusRepository.save(new UserStatus(user5, Instant.parse("2026-06-07T12:00:00Z")));
        em.flush();
        em.clear();

        // when
        List<User> result = userRepository.findAllWithProfileAndStatus();

        // then
        assertThat(result)
                .hasSize(2);

        assertThat(result)
                .extracting(User::getEmail)
                .containsExactlyInAnyOrder(
                        "user4@example.com",
                        "user5@example.com"
                );

        result.forEach(user -> {
            assertThat(Hibernate.isInitialized(user.getProfile()))
                    .isTrue();

            assertThat(Hibernate.isInitialized(user.getStatus()))
                    .isTrue();
        });
    }

    @Test
    @DisplayName("전체조회 - userStatus가 없으면 조회 안됨")
    void findAllWithProfileAndStatus_fail() {

        // given
        BinaryContent profile = new BinaryContent("a.png", 1L, "png");

        userRepository.save(createUser("user6", "user6@test.com", profile));

        em.flush();
        em.clear();

        // when
        List<User> result = userRepository.findAllWithProfileAndStatus();

        // then
        assertThat(result).isEmpty();
    }
}