package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/readstatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> create(
            @RequestPart("ReadStatusCreate") ReadStatusCreateRequest request) {
        return ResponseEntity.ok(readStatusService.create(request));
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<ReadStatus> update(
            @PathVariable UUID id,
            @RequestPart("ReadStatusUpdate") ReadStatusUpdateRequest request
    ){
        return ResponseEntity.ok(readStatusService.update(id, request));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        readStatusService.delete(id);
    }

}
