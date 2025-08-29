package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.mapper.TopicMapper;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepo;
    private final TopicMapper mapper;

    @Transactional
    @Override
    public TopicResponse create(TopicCreateRequest req) {
        log.info("Creating topic title={}", req.title());
        assertTitleUnique(req.title());
        return mapper.toResponse(topicRepo.save(mapper.toEntity(req)));
    }

    @Override
    public Page<TopicResponse> list(Pageable p) {
        log.info("Listing topics page={} size={} sort={}",
                p.getPageNumber(), p.getPageSize(), p.getSort());
        return topicRepo.findAll(p).map(mapper::toResponse);
    }

    @Override
    public TopicResponse getById(Long id) {
        log.info("Fetching topic id={}", id);
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting topic id={}", id);
        if (!topicRepo.existsById(id)) throw new NotFoundException("topic.not-found");
        topicRepo.deleteById(id);
    }

    private Topic getOrThrow(Long id) {
        return topicRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("topic.not-found"));
    }

    private void assertTitleUnique(String title) {
        if (topicRepo.existsByTitleIgnoreCase(title))
            throw new ConflictException("topic.title-already-exists");
    }
}
