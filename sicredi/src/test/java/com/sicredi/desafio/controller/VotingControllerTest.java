package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.assembler.VoteCountModelAssembler;
import com.sicredi.desafio.assembler.VoteModelAssembler;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import com.sicredi.desafio.dto.response.VoteResponse;
import com.sicredi.desafio.service.VotingService;
import com.sicredi.desafio.service.enumerations.VoteChoice;
import com.sicredi.desafio.service.enumerations.VoteResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VotingController.class)
public class VotingControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    VotingService service;
    @MockBean
    VoteModelAssembler assembler;
    @MockBean
    VoteCountModelAssembler countModelAssembler;

    private static final String BASE = "/api/v1";

    @Test
    void vote_returns201_withBody() throws Exception {
        long sessionId = 10L;
        var req = new VoteCreateRequest(VoteChoice.SIM, "11122233344");

        var body = new VoteResponse(99L, sessionId, "11122233344", VoteChoice.SIM,
                LocalDateTime.of(2025, 1, 1, 12, 0, 0));
        var model = EntityModel.of(body, Link.of(BASE + "/sessions/" + sessionId + "/votes/99").withSelfRel());

        Mockito.when(service.vote(eq(sessionId), any(VoteCreateRequest.class))).thenReturn(body);
        Mockito.when(assembler.toModel(eq(body))).thenReturn(model);

        mvc.perform(post(BASE + "/sessions/{sessionId}/votes", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.sessionId", is(10)))
                .andExpect(jsonPath("$.associateId", is("11122233344")))
                .andExpect(jsonPath("$._links.self.href", is(BASE + "/sessions/10/votes/99")));
    }

    @Test
    void count_returns200_withBody() throws Exception {
        long sessionId = 10L;
        var body = new VoteCountResponse(sessionId, 5L, 5, 2, VoteResult.APPROVED);
        var model = EntityModel.of(body, Link.of(BASE + "/sessions/" + sessionId + "/votes/count").withSelfRel());

        Mockito.when(service.count(sessionId)).thenReturn(body);
        Mockito.when(countModelAssembler.toModel(body)).thenReturn(model);

        mvc.perform(get(BASE + "/sessions/{sessionId}/votes/count", sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.sessionId", is(10)))
                .andExpect(jsonPath("$.yes", is(5)))
                .andExpect(jsonPath("$.no", is(2)))
                .andExpect(jsonPath("$._links.self.href", is(BASE + "/sessions/10/votes/count")));
    }

    @Test
    void checkIfCanOpen_returns200() throws Exception {
        long topicId = 1L;
        Mockito.when(service.canOpenSession(eq(topicId), any(LocalDateTime.class))).thenReturn(true);

        mvc.perform(post(BASE + "/topics/{topicId}/sessions:check-open", topicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canOpen", is(true)));
    }

    @Test
    void isSessionOpenNow_returns200() throws Exception {
        long sessionId = 10L;
        Mockito.when(service.isSessionOpenNow(eq(sessionId), any(LocalDateTime.class))).thenReturn(false);

        mvc.perform(get(BASE + "/sessions/{sessionId}/open-now", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openNow", is(false)));
    }
}
