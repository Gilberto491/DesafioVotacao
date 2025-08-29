package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Topics")
@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "Create new Topic")
    @StandardErrors
    @ApiResponse(responseCode = "201", description = "Created")
    @PostMapping
    public ResponseEntity<TopicResponse> create(@Valid @RequestBody TopicCreateRequest req) {
        TopicResponse resp = topicService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
