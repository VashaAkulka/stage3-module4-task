package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.AuthorDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.mapper.AuthorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthorService implements BaseExtendService<AuthorDTO, Long> {
    private AuthorRepository repository;

    @Override
    public List<AuthorDTO> readByNewsId(Long id) throws NoSuchElementException {
        List<AuthorModel> authorModel = repository.findAuthorByNewsId(id);
        if (authorModel == null || authorModel.isEmpty()) throw new NoSuchElementException("No such author");
        else return AuthorMapper.INSTANCE.authorListToAuthorDtoList(authorModel);
    }

    @Override
    public List<AuthorDTO> readAll() {
        return AuthorMapper.INSTANCE.authorListToAuthorDtoList(repository.findAll());
    }

    @Override
    public AuthorDTO readById(Long id) throws NoSuchElementException {
        return AuthorMapper.INSTANCE.authorToAuthorDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such author")));
    }

    @Override
    public AuthorDTO create(AuthorDTO createRequest) {
        AuthorModel authorModel = new AuthorModel();
        authorModel.setName(createRequest.getName());
        authorModel.setCreateDate(LocalDateTime.now());
        authorModel.setLastUpdateDate(LocalDateTime.now());
        authorModel.setNews(new HashSet<>());

        return AuthorMapper.INSTANCE.authorToAuthorDto(repository.save(authorModel));
    }

    @Override
    public AuthorDTO update(AuthorDTO updateRequest, Long id) throws NoSuchElementException {
        AuthorModel authorModel = repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such author"));
        authorModel.setName(updateRequest.getName());
        authorModel.setLastUpdateDate(LocalDateTime.now());

        return AuthorMapper.INSTANCE.authorToAuthorDto(repository.save(authorModel));
    }

    @Override
    public boolean deleteById(Long id) {
        AuthorModel authorModel = repository.findById(id).orElse(null);
        if (authorModel == null) return false;
        else {
            repository.deleteById(id);
            return true;
        }
    }
}
