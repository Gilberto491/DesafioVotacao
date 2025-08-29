package com.sicredi.desafio.service;

import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;

public interface TopicService {

    TopicResponse create(TopicCreateRequest req);

}
