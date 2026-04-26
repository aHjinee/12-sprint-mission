package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User userCreate(
            @RequestPart("userData") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

        if (profileImage != null && !profileImage.isEmpty()) {
            profileRequest = Optional.of(new BinaryContentCreateRequest(
                    profileImage.getOriginalFilename(),
                    profileImage.getContentType(),
                    profileImage.getBytes()
            ));
        }


        return userService.create(userCreateRequest, profileRequest);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<User> userUpdate(
            @PathVariable UUID id,
            @RequestPart("userUpdateData") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws IOException {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.empty();

        if (profileImage != null && !profileImage.isEmpty()) {
            profileRequest = Optional.of(new BinaryContentCreateRequest(
                    profileImage.getOriginalFilename(),
                    profileImage.getContentType(),
                    profileImage.getBytes()
            ));
        }
        User updateUser = userService.update(id, userUpdateRequest, profileRequest);
        return ResponseEntity.ok(updateUser);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void userDelete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = userService.find(id);
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/userstatus/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<UserStatus> updateUserStatus(
            @PathVariable UUID id,
            @RequestBody UserStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(userStatusService.updateByUserId(id, request));

    }

}
