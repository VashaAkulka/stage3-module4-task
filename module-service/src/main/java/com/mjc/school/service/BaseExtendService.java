package com.mjc.school.service;

import com.mjc.school.service.exception.NoSuchElementException;

import java.util.List;

public interface BaseExtendService<R, K> extends BaseService<R, K> {
    List<R> readByNewsId(K id) throws NoSuchElementException;
}
