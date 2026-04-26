package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @RequestMapping(value = "/publiccreate", method = RequestMethod.POST)
    public ResponseEntity<Channel> publicChannelCreate(
            @RequestPart("publicCreateData") PublicChannelCreateRequest request
    ) {
        return ResponseEntity.ok(channelService.create(request));
    }

    @RequestMapping(value = "/privatecreate", method = RequestMethod.POST)
    public ResponseEntity<Channel> privateChannelCreate(
            @RequestPart("privateCreateData") PrivateChannelCreateRequest request
    ) {
        return ResponseEntity.ok(channelService.create(request));
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Channel> channelUpdate(
            @PathVariable UUID id,
            @RequestPart("updateChannel") PublicChannelUpdateRequest request
    ) {
       return ResponseEntity.ok(channelService.update(id, request));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void channelDelete(@PathVariable UUID id) {
        channelService.delete(id);
    }

    @RequestMapping(value = "/{userid}", method = RequestMethod.GET)
    public ResponseEntity<List<ChannelDto>> getChannelByUserId(@PathVariable UUID userid){
        List<ChannelDto> channels = channelService.findAllByUserId(userid);
        return ResponseEntity.ok(channels);
    }
}
