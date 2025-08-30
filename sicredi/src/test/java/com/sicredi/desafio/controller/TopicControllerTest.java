package com.sicredi.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sicredi.desafio.assembler.TopicModelAssembler;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.helpers.PageStub;
import com.sicredi.desafio.service.TopicService;
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
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TopicController.class)
public class TopicControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockBean
    TopicService topicService;
    @MockBean
    TopicModelAssembler assembler;

    @Test
    void create_returns201_withLocation_andBody() throws Exception {
        // given
        var req = new TopicCreateRequest("Pauta 1", "Descrição Pauta 1");
        var resp = new TopicResponse(1L, "Pauta 1", "Descrição Pauta 1", TopicStatus.PENDING, LocalDateTime.now());
        var model = EntityModel.of(resp, Link.of("/topics/1").withSelfRel());

        Mockito.when(topicService.create(any(TopicCreateRequest.class))).thenReturn(resp);
        Mockito.when(assembler.toModel(eq(resp))).thenReturn(model);

        // when/then
        mvc.perform(post("/api/v1/topics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/topics/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Pauta 1")));
    }

    @Test
    void list_returns200_withPagedModel() throws Exception {
        var item = new TopicResponse(1L, "Pauta 1", "Descrição Pauta 1", TopicStatus.PENDING, LocalDateTime.now());
        var em = EntityModel.of(item, Link.of("/topics/1").withSelfRel());

        Mockito.when(topicService.list(any())).thenReturn(new PageStub<>(List.of(item)));
        Mockito.when(assembler.toModel(any(TopicResponse.class))).thenReturn(em);

        mvc.perform(get("/api/v1/topics?page=0&size=1"))
                .andExpect(jsonPath("$._embedded.topicResponseList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.topicResponseList[0].title").value("Pauta 1"))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost/api/v1/topics?page=0&size=1"));
    }

    @Test
    void getById_returns200_withBody() throws Exception {
        var resp = new TopicResponse(1L, "Pauta 1", "Descrição Pauta 1", TopicStatus.PENDING, LocalDateTime.now());
        var model = EntityModel.of(resp, Link.of("/topics/1").withSelfRel());

        Mockito.when(topicService.getById(1L)).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(get("/api/v1/topics/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Pauta 1")));
    }

    @Test
    void delete_returns204() throws Exception {
        mvc.perform(delete("/api/v1/topics/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(topicService).delete(1L);
    }

}
