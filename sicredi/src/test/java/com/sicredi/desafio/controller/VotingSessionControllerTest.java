package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.assembler.SessionModelAssembler;
import com.sicredi.desafio.dto.request.SessionCreateRequest;
import com.sicredi.desafio.dto.response.SessionResponse;
import com.sicredi.desafio.helpers.ApiPaths;
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

import static com.sicredi.desafio.helpers.TestConstants.SESSION_ID;
import static com.sicredi.desafio.helpers.TestConstants.TOPIC_ID;
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

    @Test
    void openSession_withBody_returns201() throws Exception {
        var req = new SessionCreateRequest(15);
        var resp = new SessionResponse(SESSION_ID, TOPIC_ID, Instant.now(), Instant.now());
        var model = EntityModel.of(resp, Link.of(ApiPaths.session(TOPIC_ID, SESSION_ID)).withSelfRel());

        Mockito.when(service.openSession(eq(TOPIC_ID), eq(req))).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(post(ApiPaths.sessions(TOPIC_ID))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.topicId", is(1)))
                .andExpect(jsonPath("$._links.self.href", is(ApiPaths.session(TOPIC_ID, SESSION_ID))));
    }

    @Test
    void openSession_withoutBody_returns201() throws Exception {
        var resp = new SessionResponse(SESSION_ID, TOPIC_ID, Instant.now(), Instant.now());
        var model = EntityModel.of(resp, Link.of(ApiPaths.session(TOPIC_ID, SESSION_ID)).withSelfRel());

        Mockito.when(service.openSession(eq(TOPIC_ID), isNull())).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(post(ApiPaths.sessions(TOPIC_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is((int) SESSION_ID)))
                .andExpect(jsonPath("$.topicId", is((int) TOPIC_ID)));
    }
}
