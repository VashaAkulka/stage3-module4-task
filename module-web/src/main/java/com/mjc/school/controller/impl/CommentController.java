package com.mjc.school.controller.impl;

import com.mjc.school.controller.BaseExtendController;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.CommentDTO;
import com.mjc.school.service.dto.TagDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@RequestMapping("/comment")
public class CommentController implements BaseExtendController<CommentDTO, Long> {
    private BaseExtendService<CommentDTO, Long> service;

    @Override
    @GetMapping("/news/{id}")
    public ResponseEntity<List<CommentDTO>> readByNewsId(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readByNewsId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<PagedModel<CommentDTO>> readAll(@RequestParam(value = "page", required = false) Integer page,
                                                      @RequestParam(value = "sort", required = false) String sort,
                                                      @RequestParam(value = "limit", required = false) Integer limit) {

        List<CommentDTO> commentDTOList = service.readAll();
        if (commentDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            if (page == null || sort == null || limit == null) {
                return ResponseEntity.ok(PagedModel.of(commentDTOList, new PagedModel.PageMetadata(0, 0, commentDTOList.size())));
            }

            int startIndex = (page - 1) * limit;
            int endIndex = Math.min(startIndex + limit, commentDTOList.size());
            List<CommentDTO> paginatedCommentDTOList = commentDTOList.subList(startIndex, endIndex);

            if (sort.equals("asc")) {
                paginatedCommentDTOList.sort(Comparator.comparing(CommentDTO::getContent));
            } else if (sort.equals("desc")) {
                paginatedCommentDTOList.sort(Comparator.comparing(CommentDTO::getContent).reversed());
            }

            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(limit, page, commentDTOList.size());
            PagedModel<CommentDTO> pagedModel = PagedModel.of(paginatedCommentDTOList, metadata);

            if (endIndex < commentDTOList.size()) {
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
    public ResponseEntity<CommentDTO> readById(@PathVariable Long id) throws NoSuchElementException {
        return ResponseEntity.ok(service.readById(id));
    }

    @Override
    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody @Valid CommentDTO createRequest,
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
    public ResponseEntity<CommentDTO> update(@RequestBody @Valid CommentDTO updateRequest,
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
