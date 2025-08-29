package com.sicredi.desafio.assembler;

import com.sicredi.desafio.controller.TopicController;
import com.sicredi.desafio.controller.VotingController;
import com.sicredi.desafio.dto.response.VoteCountResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VoteCountModelAssembler implements RepresentationModelAssembler<VoteCountResponse, EntityModel<VoteCountResponse>> {

    @Override
    public EntityModel<VoteCountResponse> toModel(VoteCountResponse c) {
        return EntityModel.of(c,
                linkTo(methodOn(VotingController.class).isSessionOpenNow(c.sessionId())).withRel("open-now"),
                linkTo(methodOn(TopicController.class).getById(c.topicId())).withRel("topic")
        );
    }
}
