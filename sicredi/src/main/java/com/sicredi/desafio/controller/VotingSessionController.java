package com.sicredi.desafio.controller;

import com.sicredi.desafio.assembler.SessionModelAssembler;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sicredi.desafio.constants.ApiConstants.BASE;

@Slf4j
@Validated
@Tag(name = "VoteSession")
@RestController
@RequestMapping(BASE)
@RequiredArgsConstructor
public class VotingSessionController {

    private final VotingSessionService service;
    private final SessionModelAssembler assembler;

    @Operation(summary = "Open session for a topic")
    @ApiResponse(responseCode = "201", description = "Created")
    @StandardErrors
    @PostMapping("/topics/{topicId}/sessions")
    public ResponseEntity<EntityModel<SessionResponse>> openSession(
            @PathVariable Long topicId,
            @RequestBody(required = false) SessionCreateRequest req) {
        log.info("POST /topics/{}/sessions start duration={}min",
                topicId, req != null ? req.durationMinutes() : "default");
        EntityModel<SessionResponse> model = assembler.toModel(service.openSession(topicId, req));
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }
}
