package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "VoteSession")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VotingSessionController {

    private final VotingSessionService service;

    @Operation(summary = "Open session for a topic")
    @ApiResponse(responseCode = "201", description = "Created")
    @StandardErrors
    @PostMapping("/topics/{topicId}/sessions")
    public ResponseEntity<SessionResponse> openSession(
            @PathVariable Long topicId,
            @RequestBody(required = false) SessionCreateRequest req) {
        var resp = service.openSession(topicId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
