package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseExtendController;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.AuthorDTO;
import com.mjc.school.service.dto.CommentDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
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
    public ResponseEntity<PagedModel<AuthorDTO>> readAll(@RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "sort", required = false) String sort,
                                                          @RequestParam(value = "limit", required = false) Integer limit) {

        List<AuthorDTO> authorDTOList = service.readAll();
        if (authorDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            if (page == null || sort == null || limit == null) {
                return ResponseEntity.ok(PagedModel.of(authorDTOList, new PagedModel.PageMetadata(0, 0, authorDTOList.size())));
            }

            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, authorDTOList.size());
            List<AuthorDTO> paginatedAuthorDTOList = authorDTOList.subList(startIndex, endIndex);

            if (sort.equals("asc")) {
                paginatedAuthorDTOList.sort(Comparator.comparing(AuthorDTO::getName));
            } else if (sort.equals("desc")) {
                paginatedAuthorDTOList.sort(Comparator.comparing(AuthorDTO::getName).reversed());
            }

            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(limit, page, authorDTOList.size());
            PagedModel<AuthorDTO> pagedModel = PagedModel.of(paginatedAuthorDTOList, metadata);

            if (endIndex < authorDTOList.size()) {
                String nextLink = String.format("/news?page=%d&limit=%d&sort=%s", (page + 1), limit, sort);
                pagedModel.add(Link.of(nextLink, LinkRelation.of("next")));
            }

            if (startIndex > 0) {
                String previousLink = String.format("/news?page=%d&limit=%d&sort=%s", (page - 1), limit, sort);
                pagedModel.add(Link.of(previousLink, LinkRelation.of("previous")));
            }

            return ResponseEntity.ok(pagedModel);
        }
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
