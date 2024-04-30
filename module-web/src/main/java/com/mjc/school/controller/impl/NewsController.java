package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController implements BaseController<NewsDTO, Long> {
    BaseService<NewsDTO, Long> service;

    @Override
    @GetMapping
    public ResponseEntity<List<NewsDTO>> readAll() {
         List<NewsDTO> newsDTOList = service.readAll();
         if (newsDTOList.isEmpty()) return ResponseEntity.noContent().build();
         else return ResponseEntity.ok(newsDTOList);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<NewsDTO> create(@RequestBody @Valid NewsDTO createRequest,
                                          BindingResult bindingResult) throws ValidationException, NoSuchElementException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()
                    .toString());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(createRequest));
    }

    @Override
    @PatchMapping
    public ResponseEntity<NewsDTO> update(@RequestBody @Valid NewsDTO updateRequest,
                                          @PathVariable Long id,
                                          BindingResult bindingResult) throws ValidationException, NoSuchElementException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()
                    .toString());
        }

        return ResponseEntity.ok(service.update(updateRequest, id));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        if (service.deleteById(id)) return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(true);
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(false);
    }
}
