package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseExtendController;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.dto.TagDTO;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
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
    public ResponseEntity<PagedModel<TagDTO>> readAll(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "sort", required = false) String sort,
                                                       @RequestParam(value = "limit", required = false) Integer limit) {

        List<TagDTO> tagDTOList = service.readAll();
        if (tagDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            if (page == null || sort == null || limit == null) {
                return ResponseEntity.ok(PagedModel.of(tagDTOList, new PagedModel.PageMetadata(0, 0, tagDTOList.size())));
            }

            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, tagDTOList.size());
            List<TagDTO> paginatedTagDTOList = tagDTOList.subList(startIndex, endIndex);

            if (sort.equals("asc")) {
                paginatedTagDTOList.sort(Comparator.comparing(TagDTO::getName));
            } else if (sort.equals("desc")) {
                paginatedTagDTOList.sort(Comparator.comparing(TagDTO::getName).reversed());
            }

            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(limit, page, tagDTOList.size());
            PagedModel<TagDTO> pagedModel = PagedModel.of(paginatedTagDTOList, metadata);

            if (endIndex < tagDTOList.size()) {
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
    public ResponseEntity<TagDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@RequestBody @Valid TagDTO createRequest,
                                         BindingResult bindingResult) throws ValidationException, NoSuchElementException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList()
                    .toString());
        }

        return service.create(createRequest);
    }

    @Override
    @PatchMapping("/{id}")
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        if (!service.deleteById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id " + id + " not found");
    }
}
