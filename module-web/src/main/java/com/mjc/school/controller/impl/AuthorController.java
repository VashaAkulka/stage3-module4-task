package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseExtendController;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.AuthorDTO;
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
@RequestMapping("/author")
public class AuthorController implements BaseExtendController<AuthorDTO, Long> {
    private BaseExtendService<AuthorDTO, Long> service;

    @Override
    @GetMapping("/news/{id}")
    public ResponseEntity<List<AuthorDTO>> readByNewsId(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readByNewsId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> readAll() {
        List<AuthorDTO> authorDTOList = service.readAll();
        if (authorDTOList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(authorDTOList);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<AuthorDTO> create(@RequestBody @Valid AuthorDTO createRequest,
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
    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDTO> update(@RequestBody @Valid AuthorDTO updateRequest,
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        if (service.deleteById(id)) return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(true);
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(false);
    }
}
