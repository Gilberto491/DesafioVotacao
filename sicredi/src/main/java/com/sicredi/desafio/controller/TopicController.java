package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.service.impl.TopicServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicServiceImpl topicServiceImpl;

    @PostMapping
    public ResponseEntity<TopicResponse> create(@Valid @RequestBody TopicCreateRequest req) {
        TopicResponse resp = topicServiceImpl.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
