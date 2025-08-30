package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.assembler.SessionModelAssembler;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.service.VotingSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VotingSessionController.class)
public class VotingSessionControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    VotingSessionService service;
    @MockBean
    SessionModelAssembler assembler;

    private static final String BASE = "/api/v1";

    @Test
    void openSession_withBody_returns201() throws Exception {
        long topicId = 1L;
        var req = new SessionCreateRequest(15);

        var resp = new SessionResponse(10L, topicId, Instant.now(), Instant.now());

        var model = EntityModel.of(resp, Link.of(BASE + "/topics/" + topicId + "/sessions/10").withSelfRel());

        Mockito.when(service.openSession(eq(topicId), eq(req))).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(post(BASE + "/topics/{topicId}/sessions", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.topicId", is(1)))
                .andExpect(jsonPath("$._links.self.href", is(BASE + "/topics/1/sessions/10")));
    }

    @Test
    void openSession_withoutBody_returns201() throws Exception {
        long topicId = 1L;
        var resp = new SessionResponse(11L, topicId, Instant.now(), Instant.now());

        var model = EntityModel.of(resp, Link.of(BASE + "/topics/1/sessions/11").withSelfRel());

        Mockito.when(service.openSession(eq(topicId), isNull())).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(post(BASE + "/topics/{topicId}/sessions", topicId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.topicId", is(1)));
    }
}
