package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepo;

    @Override
    @Transactional
    public TopicResponse create(TopicCreateRequest req) {
        if (topicRepo.existsByTitleIgnoreCase(req.title())) {
            throw new ConflictException("Topic title already exists");
        }

        Topic t = Topic.builder()
                .title(req.title())
                .description(req.description())
                .status(TopicStatus.OPEN)
                .build();

        t = topicRepo.save(t);

        return new TopicResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TopicResponse> list(Pageable pageable) {
        return topicRepo.findAll(pageable)
                .map(t -> new TopicResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt()));
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getById(Long id) {
        Topic t = topicRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic not found"));
        return new TopicResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(), t.getCreatedAt());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!topicRepo.existsById(id)) throw new NotFoundException("Topic not found");
        topicRepo.deleteById(id);
    }
}
