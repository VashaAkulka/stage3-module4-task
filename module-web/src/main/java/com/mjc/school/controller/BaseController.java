package com.mjc.school.controller;

import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface BaseController<R, K> {
    ResponseEntity<List<R>> readAll();

    ResponseEntity<R> readById(K id) throws NoSuchElementException;

    ResponseEntity<R> create(R createRequest, BindingResult bindingResult) throws ValidationException, NoSuchElementException;

    ResponseEntity<R> update(R updateRequest, Long id, BindingResult bindingResult) throws NoSuchElementException, ValidationException;

    ResponseEntity<Boolean> deleteById(K id) throws NoSuchElementException;
}
