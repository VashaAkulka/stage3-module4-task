package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseExtendController;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.TagDTO;
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
@RequestMapping("/tag")
public class TagController implements BaseExtendController<TagDTO, Long> {
    private BaseExtendService<TagDTO, Long> service;

    @Override
    @GetMapping("/news/{id}")
    public ResponseEntity<List<TagDTO>> readByNewsId(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readByNewsId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TagDTO>> readAll() {
        List<TagDTO> tagDTOList = service.readAll();
        if (tagDTOList.isEmpty()) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(tagDTOList);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<TagDTO> create(@RequestBody @Valid TagDTO createRequest,
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
    public ResponseEntity<TagDTO> update(@RequestBody @Valid TagDTO updateRequest,
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
