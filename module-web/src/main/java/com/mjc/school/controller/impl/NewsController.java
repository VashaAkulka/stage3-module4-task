package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseController;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/news")
public class NewsController implements BaseController<NewsDTO, Long> {
    private BaseService<NewsDTO, Long> service;

    @Override
    @GetMapping
    public ResponseEntity<PagedModel<NewsDTO>> readAll(@RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "sort", required = false) String sort,
                                                 @RequestParam(value = "limit", required = false) Integer limit) {

        List<NewsDTO> newsDTOList = service.readAll();
        if (newsDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            if (page == null || sort == null || limit == null) {
                return ResponseEntity.ok(PagedModel.of(newsDTOList, new PagedModel.PageMetadata(0, 0, newsDTOList.size())));
            }

            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, newsDTOList.size());
            List<NewsDTO> paginatedNewsDTOList = newsDTOList.subList(startIndex, endIndex);

            if (sort.equals("asc")) {
                paginatedNewsDTOList.sort(Comparator.comparing(NewsDTO::getTitle));
            } else if (sort.equals("desc")) {
                paginatedNewsDTOList.sort(Comparator.comparing(NewsDTO::getTitle).reversed());
            }

            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(limit, page, newsDTOList.size());
            PagedModel<NewsDTO> pagedModel = PagedModel.of(paginatedNewsDTOList, metadata);

            if (endIndex < newsDTOList.size()) {
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
    public ResponseEntity<NewsDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @PatchMapping("/{id}")
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        if (service.deleteById(id)) return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(true);
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(false);
    }
}
