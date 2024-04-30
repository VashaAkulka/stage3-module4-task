package com.mjc.school.controller;

import com.mjc.school.service.exception.NoSuchElementException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BaseExtendController<R, K> extends BaseController<R, K> {
    ResponseEntity<List<R>> readByNewsId(K id) throws NoSuchElementException;
}
