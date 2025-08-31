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

import static com.sicredi.desafio.helpers.ApiPaths.checkOpen;
import static com.sicredi.desafio.helpers.ApiPaths.openNow;
import static com.sicredi.desafio.helpers.ApiPaths.vote;
import static com.sicredi.desafio.helpers.ApiPaths.voteCount;
import static com.sicredi.desafio.helpers.ApiPaths.votes;
import static com.sicredi.desafio.helpers.TestConstants.CPF;
import static com.sicredi.desafio.helpers.TestConstants.SESSION_ID_10;
import static com.sicredi.desafio.helpers.TestConstants.TOPIC_ID;
import static com.sicredi.desafio.helpers.TestConstants.VOTE_ID;
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

    @Test
    void vote_returns201_withBody() throws Exception {
        var req = new VoteCreateRequest(VoteChoice.SIM, CPF);

        var body = new VoteResponse(99L, SESSION_ID_10, CPF, VoteChoice.SIM,
                LocalDateTime.of(2025, 1, 1, 12, 0, 0));
        var model = EntityModel.of(body, Link.of(vote(SESSION_ID_10, VOTE_ID)).withSelfRel());

        Mockito.when(service.vote(eq(SESSION_ID_10), any(VoteCreateRequest.class))).thenReturn(body);
        Mockito.when(assembler.toModel(eq(body))).thenReturn(model);

        mvc.perform(post(votes(SESSION_ID_10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.sessionId", is(10)))
                .andExpect(jsonPath("$.associateId", is(CPF)))
                .andExpect(jsonPath("$._links.self.href").value(vote(SESSION_ID_10, VOTE_ID)));
    }

    @Test
    void count_returns200_withBody() throws Exception {
        var body = new VoteCountResponse(SESSION_ID_10, 5L, 5, 2, VoteResult.APPROVED);
        var model = EntityModel.of(body, Link.of(voteCount(SESSION_ID_10)).withSelfRel());

        Mockito.when(service.count(SESSION_ID_10)).thenReturn(body);
        Mockito.when(countModelAssembler.toModel(body)).thenReturn(model);

        mvc.perform(get(voteCount(SESSION_ID_10)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.sessionId", is(10)))
                .andExpect(jsonPath("$.yes", is(5)))
                .andExpect(jsonPath("$.no", is(2)))
                .andExpect(jsonPath("$._links.self.href").value(voteCount(SESSION_ID_10)));
    }

    @Test
    void checkIfCanOpen_returns200() throws Exception {
        Mockito.when(service.canOpenSession(eq(TOPIC_ID), any(LocalDateTime.class))).thenReturn(true);

        mvc.perform(post(checkOpen(TOPIC_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canOpen").value(true));
    }

    @Test
    void isSessionOpenNow_returns200() throws Exception {
        Mockito.when(service.isSessionOpenNow(eq(SESSION_ID_10), any(LocalDateTime.class))).thenReturn(false);

        mvc.perform(get(openNow(SESSION_ID_10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openNow").value(false));
    }
}
