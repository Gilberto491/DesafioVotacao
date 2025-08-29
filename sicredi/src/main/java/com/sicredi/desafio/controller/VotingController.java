package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.response.OpenSessionCheckResponse;
import com.sicredi.desafio.dto.response.SessionOpenNowResponse;
import com.sicredi.desafio.service.impl.VotingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VotingController {

    private final VotingServiceImpl service;

    @PostMapping("/topics/{topicId}/sessions:check-open")
    public ResponseEntity<?> checkIfCanOpen(@PathVariable Long topicId) {
        boolean can = service.canOpenSession(topicId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new OpenSessionCheckResponse(can));
    }

    @GetMapping("/sessions/{sessionId}/open-now")
    public ResponseEntity<SessionOpenNowResponse> isSessionOpenNow(@PathVariable Long sessionId) {
        boolean open = service.isSessionOpenNow(sessionId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new SessionOpenNowResponse(open));
    }

}
