package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepo;

    @Transactional
    public TopicResponse create(TopicCreateRequest req) {
        if (topicRepo.existsByTitleIgnoreCase(req.title())) {
            throw new IllegalStateException("Topic title already exists");
        }

        Topic t = Topic.builder()
                .title(req.title())
                .description(req.description())
                .status(TopicStatus.OPEN)
                .build();

        t = topicRepo.save(t);

        return new TopicResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus());
    }
}
