package com.sprint.mission.discodeit.controller;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Message> create(
            @RequestPart("messageData") MessageCreateRequest request,
            @RequestPart(value = "binaryContentCreateRequests", required = false) List<MultipartFile> binaryContentCreateRequests
    ) throws IOException {
        List<BinaryContentCreateRequest> binaryContents = new ArrayList<>();
        BinaryContentCreateRequest binaryContent = null;

        if (binaryContentCreateRequests != null && !binaryContentCreateRequests.isEmpty()) {

            for (MultipartFile MultiBinaryContent : binaryContentCreateRequests) {
                binaryContent = new BinaryContentCreateRequest(
                        MultiBinaryContent.getOriginalFilename(),
                        MultiBinaryContent.getContentType(),
                        MultiBinaryContent.getBytes()
                );

                binaryContents.add(binaryContent);
            }

        }
        return ResponseEntity.ok(messageService.create(request, binaryContents));
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Message> update(
            @PathVariable UUID id,
            @RequestPart("messageUpdateData") MessageUpdateRequest request
    ){
        return ResponseEntity.ok(messageService.update(id, request));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        messageService.delete(id);
    }

    @RequestMapping(value = "/{channelid}", method = RequestMethod.GET)
    public ResponseEntity<List<Message>> getMessageByChannelId(
            @PathVariable UUID channelid
    ){
        return ResponseEntity.ok(messageService.findAllByChannelId(channelid));
    }
}
