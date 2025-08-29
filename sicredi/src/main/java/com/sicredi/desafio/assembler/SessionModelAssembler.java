package com.sicredi.desafio.assembler;

import com.sicredi.desafio.controller.TopicController;
import com.sicredi.desafio.controller.VotingController;
import com.sicredi.desafio.dto.response.SessionResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SessionModelAssembler implements RepresentationModelAssembler<SessionResponse, EntityModel<SessionResponse>> {

    @Override
    public EntityModel<SessionResponse> toModel(SessionResponse s) {
        return EntityModel.of(s,
                linkTo(methodOn(VotingController.class).checkIfCanOpen(s.id())).withRel("check-open"),
                linkTo(methodOn(VotingController.class).isSessionOpenNow(s.id())).withRel("open-now"),
                linkTo(methodOn(VotingController.class).vote(s.id(), null)).withRel("vote"),
                linkTo(methodOn(VotingController.class).count(s.id())).withRel("count-votes"),
                linkTo(methodOn(TopicController.class).getById(s.topicId())).withRel("topic")
        );
    }
}
