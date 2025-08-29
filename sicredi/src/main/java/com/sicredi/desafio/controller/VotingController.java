package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.response.OpenSessionCheckResponse;
import com.sicredi.desafio.dto.response.SessionOpenNowResponse;
import com.sicredi.desafio.exception.ReadErrors;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.VotingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Tag(name = "Vote")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VotingController {

    private final VotingService service;

    @Operation(summary = "Checks if a session can be opened for a topic")
    @ApiResponse(responseCode = "200", description = "OK")
    @ReadErrors
    @PostMapping("/topics/{topicId}/sessions:check-open")
    public ResponseEntity<OpenSessionCheckResponse> checkIfCanOpen(@PathVariable Long topicId) {
        boolean can = service.canOpenSession(topicId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new OpenSessionCheckResponse(can));
    }

    @Operation(summary = "Checks if a session is open now")
    @ApiResponse(responseCode = "200", description = "OK")
    @ReadErrors
    @GetMapping("/sessions/{sessionId}/open-now")
    public ResponseEntity<SessionOpenNowResponse> isSessionOpenNow(@PathVariable Long sessionId) {
        boolean open = service.isSessionOpenNow(sessionId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new SessionOpenNowResponse(open));
    }
}
