package com.sicredi.desafio.service;

import com.sicredi.desafio.domain.Topic;
import com.sicredi.desafio.domain.enumerations.TopicStatus;
import com.sicredi.desafio.dto.request.TopicCreateRequest;
import com.sicredi.desafio.dto.response.TopicResponse;
import com.sicredi.desafio.exception.ConflictException;
import com.sicredi.desafio.exception.NotFoundException;
import com.sicredi.desafio.mapper.TopicMapper;
import com.sicredi.desafio.repository.TopicRepository;
import com.sicredi.desafio.service.impl.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TopicServiceImplTest {

    @Mock
    private TopicRepository topicRepo;
    @Mock
    private TopicMapper mapper;

    @InjectMocks
    private TopicServiceImpl service;

    private TopicCreateRequest req;
    private Topic entity;
    private TopicResponse resp;

    @Captor
    private ArgumentCaptor<Topic> topicCaptor;

    @BeforeEach
    void init() {
        req = new TopicCreateRequest("Pauta X", "Description Pauta X");
        entity = Topic.builder().id(1L).title("Pauta X").description("Description Pauta X").build();
        resp = new TopicResponse(1L, "Pauta X", "Description Pauta X", TopicStatus.PENDING, LocalDateTime.now());
    }

    @Test
    void create_persistsAndReturnsResponse_whenTitleIsUnique() {
        when(topicRepo.existsByTitleIgnoreCase("Pauta X")).thenReturn(false);
        when(mapper.toEntity(req)).thenReturn(entity);
        when(topicRepo.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(resp);

        var out = service.create(req);

        assertThat(out).isEqualTo(resp);
        verify(topicRepo).save(topicCaptor.capture());
        assertThat(topicCaptor.getValue().getTitle()).isEqualTo("Pauta X");
    }

    @Test
    void create_throwsConflict_whenTitleAlreadyExists() {
        when(topicRepo.existsByTitleIgnoreCase("Pauta X")).thenReturn(true);
        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("topic.title-already-exists");
        verify(topicRepo, never()).save(any());
    }

    @Test
    void getById_returnsMappedResponse_whenFound() {
        when(topicRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(resp);

        var out = service.getById(1L);

        assertThat(out).isEqualTo(resp);
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(topicRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("topic.not-found");
    }

    @Test
    void list_returnsPagedResponses() {
        var pageable = PageRequest.of(0, 2);
        when(topicRepo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(entity), pageable, 1));
        when(mapper.toResponse(entity)).thenReturn(resp);

        var page = service.list(pageable);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()).containsExactly(resp);
    }

    @Test
    void delete_removes_whenExists() {
        when(topicRepo.existsById(1L)).thenReturn(true);
        doNothing().when(topicRepo).deleteById(1L);

        service.delete(1L);

        verify(topicRepo).deleteById(1L);
    }

    @Test
    void delete_throwsNotFound_whenIdDoesNotExist() {
        when(topicRepo.existsById(42L)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(42L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("topic.not-found");
        verify(topicRepo, never()).deleteById(anyLong());
    }
}
