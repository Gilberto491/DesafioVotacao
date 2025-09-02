package com.sicredi.desafio.service.impl;

import com.sicredi.desafio.dto.PageDTO;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.mapper.TopicMapper;
import com.sicredi.desafio.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TopicCacheService {

    private final TopicMapper mapper;
    private final TopicRepository topicRepo;

    @Cacheable(cacheNames="topics", key="'p=' + #p.pageNumber + ':s=' + #p.pageSize + ':o=' + (#p.sort?:'')",
            unless="#result.content().isEmpty()")
    public PageDTO<TopicResponse> listCached(Pageable p) {
        var page = topicRepo.findAll(p).map(mapper::toResponse);
        return new PageDTO<>(page.getContent(), p.getPageNumber(), p.getPageSize(), page.getTotalElements());
    }
}
