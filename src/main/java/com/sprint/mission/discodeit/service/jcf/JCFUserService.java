package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;


    public JCFUserService() {
        data = new ArrayList<>();
    }

    @Override
    public void save(User user) {
        data.add(user);
    }

    @Override
    public User findById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new RuntimeException("findById 해당 id가 없습니다.");
    }

    @Override
    public List<User> findAll() {
        return data;
    }

    @Override
    public User update(UserUpdateDto dto) {
        User user = findById(dto.id());
        user.update(dto.username(), dto.email(), dto.password(), dto.nickname());
        return user;
    }

    @Override
    public User delete(UUID id) {
        User user = findById(id);

        data.removeIf(u -> u.getId().equals(id));
        return user;
    }
}
