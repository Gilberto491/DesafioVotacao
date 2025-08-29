package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicService {

    TopicResponse create(TopicCreateRequest req);

    Page<TopicResponse> list(Pageable pageable);

    TopicResponse getById(Long id);

    void delete(Long id);
}
