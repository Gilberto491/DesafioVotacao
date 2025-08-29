package com.sicredi.desafio.mapper;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    @Mapping(target = "active", expression = "java(t.getStatus() == TopicStatus.OPEN)")
    TopicResponse toResponse(Topic t);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "OPEN")
    Topic toEntity(TopicCreateRequest req);
}
