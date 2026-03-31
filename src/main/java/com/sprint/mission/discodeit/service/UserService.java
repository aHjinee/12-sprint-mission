package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void save(User user);
    User findById(UUID id);
    List<User> findAll();
    User update(UserUpdateDto dto);
    User delete(UUID id);

}

