package com.mjc.school.controller;

import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface BaseController<R, K> {
    ResponseEntity<PagedModel<R>> readAll(Integer page, String sort, Integer limit);

    ResponseEntity<R> readById(K id) throws NoSuchElementException;

    R create(R createRequest, BindingResult bindingResult) throws ValidationException, NoSuchElementException;

    ResponseEntity<R> update(R updateRequest, Long id, BindingResult bindingResult) throws NoSuchElementException, ValidationException;

    void deleteById(K id) throws NoSuchElementException;
}
