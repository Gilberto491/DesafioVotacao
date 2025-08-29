package com.sicredi.desafio.controller;

import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.exception.ReadErrors;
import com.sicredi.desafio.exception.StandardErrors;
import com.sicredi.desafio.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
        log.info("POST /topics start title={}", req.title());
        TopicResponse resp = topicService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(summary = "List topics")
    @ReadErrors
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<Page<TopicResponse>> list(Pageable pageable) {
        log.info("GET /topics start page={} size={} sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.OK).body(topicService.list(pageable));
    }

    @Operation(summary = "Get topic by id")
    @ReadErrors
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{id}")
    public ResponseEntity<TopicResponse> getById(@PathVariable Long id) {
        log.info("GET /topics/{} start", id);
        return ResponseEntity.ok().body(topicService.getById(id));
    }

    @Operation(summary = "Delete topic by id")
    @ReadErrors
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /topics/{} start", id);
        topicService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
