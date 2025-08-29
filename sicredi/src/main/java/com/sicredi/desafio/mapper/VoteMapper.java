package com.sicredi.desafio.mapper;

import com.sicredi.desafio.domain.Vote;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.dto.request.VoteCreateRequest;
import com.sicredi.desafio.dto.response.VoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface VoteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", source = "session.topic")
    @Mapping(target = "associateId", source = "req.cpf")
    @Mapping(target = "choice", source = "req.choice")
    @Mapping(target = "votedAt", source = "votedAt")
    Vote toEntity(VoteCreateRequest req, VotingSession session, LocalDateTime votedAt);

    @Mapping(target = "sessionId", source = "sessionId")
    VoteResponse toResponse(Vote vote, Long sessionId);
}
