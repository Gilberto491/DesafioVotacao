package com.sicredi.desafio.assembler;

import com.sicredi.desafio.controller.TopicController;
import com.sicredi.desafio.controller.VotingController;
import com.sicredi.desafio.controller.VotingSessionController;
import com.sicredi.desafio.dto.response.TopicResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TopicModelAssembler implements RepresentationModelAssembler<TopicResponse, EntityModel<TopicResponse>> {

    @Override
    public EntityModel<TopicResponse> toModel(TopicResponse topic) {
        return EntityModel.of(topic,
                linkTo(methodOn(TopicController.class).getById(topic.id())).withSelfRel(),
                linkTo(methodOn(VotingSessionController.class).openSession(topic.id(), null)).withRel("open-session"),
                linkTo(methodOn(VotingController.class).count(topic.id())).withRel("count-votes")
        );
    }
}
