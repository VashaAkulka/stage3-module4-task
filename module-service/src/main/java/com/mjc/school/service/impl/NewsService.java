package com.mjc.school.service.impl;


import com.mjc.school.repository.impl.AuthorRepository;
import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.mapper.NewsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class NewsService implements BaseService<NewsDTO, Long> {
    private NewsRepository repository;
    private TagRepository tagRepository;
    private AuthorRepository authorRepository;

    @Override
    public List<NewsDTO> readAll() {
        return NewsMapper.INSTANCE.newsListToNewsDtoList(repository.findAll());
    }

    @Override
    public NewsDTO readById(Long id) throws NoSuchElementException {
        return NewsMapper.INSTANCE.newsToNewsDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such news")));
    }

    @Override
    public NewsDTO create(NewsDTO createRequest) throws NoSuchElementException {
        NewsModel newsModel = new NewsModel();
        newsModel.setTitle(createRequest.getTitle());
        newsModel.setContent(createRequest.getContent());
        newsModel.setCreateDate(LocalDateTime.now());
        newsModel.setLastUpdateDate(LocalDateTime.now());

        Set<TagModel> tagModelSet = new HashSet<>();
        for (Long TagId : createRequest.getTagsId()) {
            tagModelSet.add(tagRepository.findById(TagId).orElseThrow(() -> new NoSuchElementException("No such tag")));
        }
        AuthorModel authorModel = authorRepository.findById(createRequest.getAuthorId()).orElseThrow(() -> new NoSuchElementException("No such author"));

        newsModel.setAuthor(authorModel);
        newsModel.setTags(tagModelSet);
        newsModel.setComments(new ArrayList<>());

        return NewsMapper.INSTANCE.newsToNewsDto(repository.save(newsModel));
    }

    @Override
    public NewsDTO update(NewsDTO updateRequest, Long id) throws NoSuchElementException {
        NewsModel newsModel = repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such news"));
        newsModel.setTitle(updateRequest.getTitle());
        newsModel.setContent(updateRequest.getContent());
        newsModel.setLastUpdateDate(LocalDateTime.now());

        Set<TagModel> tagModelSet = new HashSet<>();
        for (Long TagId : updateRequest.getTagsId()) {
            tagModelSet.add(tagRepository.findById(TagId).orElseThrow(() -> new NoSuchElementException("No such tag")));
        }
        AuthorModel authorModel = authorRepository.findById(updateRequest.getAuthorId()).orElseThrow(() -> new NoSuchElementException("No such author"));

        newsModel.setAuthor(authorModel);
        newsModel.setTags(tagModelSet);

        return NewsMapper.INSTANCE.newsToNewsDto(repository.save(newsModel));
    }

    @Override
    public boolean deleteById(Long id) {
        NewsModel newsModel = repository.findById(id).orElse(null);
        if (newsModel == null) return false;
        else {
            repository.deleteById(id);
            return true;
        }
    }
}
