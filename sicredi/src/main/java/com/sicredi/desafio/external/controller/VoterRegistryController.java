package com.sicredi.desafio.external.controller;

import com.sicredi.desafio.exception.ReadErrors;
import com.sicredi.desafio.external.dto.VoterStatusResponse;
import com.sicredi.desafio.external.service.VoterRegistryClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "CPF")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registry")
public class VoterRegistryController {

    private final VoterRegistryClient client;

    @Operation(summary = "Check CPF")
    @ReadErrors
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<VoterStatusResponse> check(@PathVariable String cpf) {
        log.info("GET /cpf/{}", cpf);
        VoterStatusResponse resp = client.checkCpf(cpf);
        return ResponseEntity.ok(resp);
    }

}
