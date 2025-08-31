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

import static com.sicredi.desafio.helpers.ApiPaths.topic;
import static com.sicredi.desafio.helpers.ApiPaths.topicRelative;
import static com.sicredi.desafio.helpers.ApiPaths.topicsPage;
import static com.sicredi.desafio.helpers.TestConstants.DESCRIPTION_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.HOST;
import static com.sicredi.desafio.helpers.TestConstants.NAME_TOPIC;
import static com.sicredi.desafio.helpers.TestConstants.TOPICS;
import static com.sicredi.desafio.helpers.TestConstants.TOPIC_ID;
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
        var req = new TopicCreateRequest(NAME_TOPIC, DESCRIPTION_TOPIC);
        var resp = new TopicResponse(1L, NAME_TOPIC, DESCRIPTION_TOPIC, TopicStatus.PENDING, LocalDateTime.now());
        var model = EntityModel.of(resp, Link.of(topicRelative(TOPIC_ID)).withSelfRel());

        Mockito.when(topicService.create(any(TopicCreateRequest.class))).thenReturn(resp);
        Mockito.when(assembler.toModel(eq(resp))).thenReturn(model);

        mvc.perform(post(TOPICS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", topicRelative(TOPIC_ID)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(NAME_TOPIC)));
    }

    @Test
    void list_returns200_withPagedModel() throws Exception {
        var item = new TopicResponse(1L, NAME_TOPIC, DESCRIPTION_TOPIC, TopicStatus.PENDING, LocalDateTime.now());
        var em = EntityModel.of(item, Link.of(topicRelative(TOPIC_ID)).withSelfRel());

        Mockito.when(topicService.list(any())).thenReturn(new PageStub<>(List.of(item)));
        Mockito.when(assembler.toModel(any(TopicResponse.class))).thenReturn(em);

        int page = 0, size = 1;

        mvc.perform(get(topicsPage(page, size)))
                .andExpect(jsonPath("$._embedded.topicResponseList[0].id").value(TOPIC_ID))
                .andExpect(jsonPath("$._embedded.topicResponseList[0].title").value(NAME_TOPIC))
                .andExpect(jsonPath("$.page.size").value(size))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$._links.self.href").value(HOST + topicsPage(page, size)));
    }

    @Test
    void getById_returns200_withBody() throws Exception {
        var resp = new TopicResponse(TOPIC_ID, NAME_TOPIC, DESCRIPTION_TOPIC, TopicStatus.PENDING, LocalDateTime.now());
        var model = EntityModel.of(resp, Link.of(topicRelative(TOPIC_ID)).withSelfRel());

        Mockito.when(topicService.getById(TOPIC_ID)).thenReturn(resp);
        Mockito.when(assembler.toModel(resp)).thenReturn(model);

        mvc.perform(get(topic(TOPIC_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is(NAME_TOPIC)));
    }

    @Test
    void delete_returns204() throws Exception {
        mvc.perform(delete(topic(TOPIC_ID)))
                .andExpect(status().isNoContent());

        Mockito.verify(topicService).delete(TOPIC_ID);
    }

}
