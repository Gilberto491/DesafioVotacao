package com.sicredi.desafio.mapper;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.VotingSession;
import com.sicredi.desafio.dto.response.SessionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "topicId", source = "topic.id")
    @Mapping(target = "opensAt",  expression = "java(s.getOpensAt().toInstant(java.time.ZoneOffset.UTC))")
    @Mapping(target = "closesAt", expression = "java(s.getClosesAt().toInstant(java.time.ZoneOffset.UTC))")
    SessionResponse toResponse(VotingSession s);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "opensAt",  source = "nowUtc")
    @Mapping(target = "closesAt", expression = "java(nowUtc.plusMinutes(durationMinutes))")
    @Mapping(target = "durationMinutes", source = "durationMinutes")
    @Mapping(target = "status", constant = "OPEN")
    VotingSession toEntity(Topic topic, LocalDateTime nowUtc, int durationMinutes);
}
