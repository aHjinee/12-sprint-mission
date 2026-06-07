package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
public class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicUserService basicUserService;

    private UUID userId;
    private String username;
    private String email;
    private String password;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        username = "test01";
        email = "test01@email.com";
        password = "password1234";

        user = new User(username, email, password, null);
        userDto = new UserDto(userId, username, email, null, null);
    }

    @Test
    @DisplayName("create 성공 - 프로필 없음")
    void create_success_withoutProfile() {
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(email)).willReturn(false);
        given(userRepository.existsByUsername(username)).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        UserDto result = basicUserService.create(request, Optional.empty());

        assertThat(result).isEqualTo(userDto);
        then(userRepository).should(times(1)).save(any(User.class));
        then(userStatusRepository).should(times(1)).save(any(UserStatus.class));
        then(userMapper).should(times(1)).toDto(any(User.class));
        then(binaryContentRepository).should(never()).save(any(BinaryContent.class));
        then(binaryContentStorage).should(never()).put(any(), any());
    }

    @Test
    @DisplayName("create 성공 - 프로필 포함")
    void create_success_withProfile() {
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        BinaryContentCreateRequest profileRequest =
                new BinaryContentCreateRequest("profile.png", "image/png", new byte[]{1, 2, 3});
        given(userRepository.existsByEmail(email)).willReturn(false);
        given(userRepository.existsByUsername(username)).willReturn(false);
        given(userMapper.toDto(any(User.class))).willReturn(userDto);

        UserDto result = basicUserService.create(request, Optional.of(profileRequest));

        assertThat(result).isEqualTo(userDto);
        then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
        then(binaryContentStorage).should(times(1)).put(any(), any());
        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("create 실패 - 이메일 중복")
    void create_fail_duplicateEmail() {
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(email)).willReturn(true);

        assertThatThrownBy(() -> basicUserService.create(request, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should(never()).save(any(User.class));
        then(userStatusRepository).should(never()).save(any(UserStatus.class));
        then(userMapper).should(never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("create 실패 - 사용자명 중복")
    void create_fail_duplicateUsername() {
        UserCreateRequest request = new UserCreateRequest(username, email, password);
        given(userRepository.existsByEmail(email)).willReturn(false);
        given(userRepository.existsByUsername(username)).willReturn(true);

        assertThatThrownBy(() -> basicUserService.create(request, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userRepository).should(never()).save(any(User.class));
        then(userStatusRepository).should(never()).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("update 성공")
    void update_success() {
        String newUsername = "test02";
        String newEmail = "test02@email.com";
        String newPassword = "newPassword1234";
        UserUpdateRequest request = new UserUpdateRequest(newUsername, newEmail, newPassword);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(newEmail)).willReturn(false);
        given(userRepository.existsByUsername(newUsername)).willReturn(false);
        given(userMapper.toDto(user)).willReturn(userDto);

        UserDto result = basicUserService.update(userId, request, Optional.empty());

        assertThat(result).isEqualTo(userDto);
        then(userRepository).should(times(1)).findById(userId);
        then(userMapper).should(times(1)).toDto(user);
    }

    @Test
    @DisplayName("update 실패 - 사용자 없음")
    void update_fail_userNotFound() {
        UserUpdateRequest request =
                new UserUpdateRequest("test02", "test02@email.com", "newPassword1234");
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> basicUserService.update(userId, request, Optional.empty()))
                .isInstanceOf(UserNotFoundException.class);

        then(userMapper).should(never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("update 실패 - 변경하려는 이메일 중복")
    void update_fail_duplicateEmail() {
        String newEmail = "duplicate@email.com";
        UserUpdateRequest request = new UserUpdateRequest("test02", newEmail, "newPassword1234");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(userRepository.existsByEmail(newEmail)).willReturn(true);

        assertThatThrownBy(() -> basicUserService.update(userId, request, Optional.empty()))
                .isInstanceOf(UserAlreadyExistsException.class);

        then(userMapper).should(never()).toDto(any(User.class));
    }

    @Test
    @DisplayName("delete 성공")
    void delete_success() {
        given(userRepository.existsById(userId)).willReturn(true);

        basicUserService.delete(userId);

        then(userRepository).should(times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("delete 실패 - 사용자 없음")
    void delete_fail_userNotFound() {
        given(userRepository.existsById(userId)).willReturn(false);

        assertThatThrownBy(() -> basicUserService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);

        then(userRepository).should(never()).deleteById(any(UUID.class));
    }
}