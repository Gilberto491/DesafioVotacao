package com.sicredi.desafio.controller;

import com.sicredi.desafio.assembler.VoteCountModelAssembler;
import com.sicredi.desafio.assembler.VoteModelAssembler;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.OpenSessionCheckResponse;
import com.sicredi.desafio.dto.response.SessionOpenNowResponse;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import com.sicredi.desafio.dto.response.VoteResponse;
import com.sicredi.desafio.exception.ReadErrors;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.VotingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Tag(name = "Vote")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VotingController {

    private final VotingService service;
    private final VoteModelAssembler assembler;
    private final VoteCountModelAssembler countModelAssembler;

    @Operation(summary = "Checks if a session can be opened for a topic")
    @ApiResponse(responseCode = "200", description = "OK")
    @ReadErrors
    @PostMapping("/topics/{topicId}/sessions:check-open")
    public ResponseEntity<OpenSessionCheckResponse> checkIfCanOpen(@PathVariable Long topicId) {
        log.info("POST /topics/{}/sessions:check-open start", topicId);
        boolean can = service.canOpenSession(topicId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new OpenSessionCheckResponse(can));
    }

    @Operation(summary = "Checks if a session is open now")
    @ApiResponse(responseCode = "200", description = "OK")
    @ReadErrors
    @GetMapping("/sessions/{sessionId}/open-now")
    public ResponseEntity<SessionOpenNowResponse> isSessionOpenNow(@PathVariable Long sessionId) {
        log.info("GET /sessions/{}/open-now start", sessionId);
        boolean open = service.isSessionOpenNow(sessionId, LocalDateTime.now(ZoneOffset.UTC));
        return ResponseEntity.ok(new SessionOpenNowResponse(open));
    }

    @Operation(summary = "Register a vote in a session")
    @ApiResponse(responseCode = "201", description = "Created")
    @StandardErrors
    @PostMapping("/sessions/{sessionId}/votes")
    public ResponseEntity<EntityModel<VoteResponse>> vote(
            @PathVariable Long sessionId,
            @Valid @RequestBody VoteCreateRequest req) {
        log.info("POST /sessions/{}/votes start choice={}", sessionId, req.choice());
        EntityModel<VoteResponse> model = assembler.toModel(service.vote(sessionId, req));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @Operation(summary = "Get vote tally for a session")
    @ApiResponse(responseCode = "200", description = "OK")
    @ReadErrors
    @GetMapping("/sessions/{sessionId}/votes/count")
    public ResponseEntity<EntityModel<VoteCountResponse>> count(@PathVariable Long sessionId) {
        log.info("GET /sessions/{}/votes/count start", sessionId);
        EntityModel<VoteCountResponse> model = countModelAssembler.toModel(service.count(sessionId));
        return ResponseEntity.ok(model);
    }
}
