package com.sicredi.desafio.assembler;

import com.sicredi.desafio.controller.VotingController;
import com.sicredi.desafio.dto.response.VoteResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VoteModelAssembler implements RepresentationModelAssembler<VoteResponse, EntityModel<VoteResponse>> {

    @Override
    public EntityModel<VoteResponse> toModel(VoteResponse v) {
        return EntityModel.of(v,
                linkTo(methodOn(VotingController.class).count(v.sessionId())).withRel("count-votes")
        );
    }
}
