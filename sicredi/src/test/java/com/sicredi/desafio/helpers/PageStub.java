package com.sicredi.desafio.helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public class PageStub<T> implements org.springframework.data.domain.Page<T> {
    private final List<T> content;

    public PageStub(List<T> content) {
        this.content = content;
    }

    @Override
    public int getTotalPages() {
        return 1;
    }

    @Override
    public long getTotalElements() {
        return content.size();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        List<U> mapped = (List<U>) content.stream().map(converter).toList();
        return new PageStub<>(mapped);
    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public int getSize() {
        return content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public org.springframework.data.domain.Sort getSort() {
        return org.springframework.data.domain.Sort.unsorted();
    }

    @Override
    public boolean isFirst() {
        return true;
    }

    @Override
    public boolean isLast() {
        return true;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Pageable nextPageable() {
        return org.springframework.data.domain.Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return org.springframework.data.domain.Pageable.unpaged();
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return content.iterator();
    }
}
