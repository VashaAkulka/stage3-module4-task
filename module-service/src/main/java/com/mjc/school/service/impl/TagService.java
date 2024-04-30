package com.mjc.school.service.impl;

import com.mjc.school.repository.impl.NewsRepository;
import com.mjc.school.repository.impl.TagRepository;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.BaseExtendService;
import com.mjc.school.service.dto.TagDTO;
import com.mjc.school.service.exception.NoSuchElementException;
import com.mjc.school.service.mapper.TagMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TagService implements BaseExtendService<TagDTO, Long> {
    private TagRepository repository;
    private NewsRepository newsRepository;

    @Override
    public List<TagDTO> readByNewsId(Long id) throws NoSuchElementException {
        List<TagModel> tagModel = repository.findTagByNewsId(id);
        if (tagModel == null || tagModel.isEmpty()) throw new NoSuchElementException("No such tag");
        else return TagMapper.INSTANCE.tagListToTagDTOList(tagModel);
    }

    @Override
    public List<TagDTO> readAll() {
        return TagMapper.INSTANCE.tagListToTagDTOList(repository.findAll());
    }

    @Override
    public TagDTO readById(Long id) throws NoSuchElementException {
        return TagMapper.INSTANCE.tagToTagDTO(repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such tag")));
    }

    @Override
    public TagDTO create(TagDTO createRequest) throws NoSuchElementException {
        TagModel tagModel = new TagModel();
        tagModel.setCreateDate(LocalDateTime.now());
        tagModel.setLastUpdateDate(LocalDateTime.now());
        tagModel.setName(createRequest.getName());

        Set<NewsModel> newsModelSet = new HashSet<>();
        for (Long newsId : createRequest.getNewsId()) {
            newsModelSet.add(newsRepository.findById(newsId).orElseThrow(() -> new NoSuchElementException("No such news")));
        }
        tagModel.setNews(newsModelSet);

        return TagMapper.INSTANCE.tagToTagDTO(repository.save(tagModel));
    }

    @Override
    public TagDTO update(TagDTO updateRequest, Long id) throws NoSuchElementException {
        TagModel tagModel = repository.findById(id).orElseThrow(() -> new NoSuchElementException("No such tag"));
        tagModel.setLastUpdateDate(LocalDateTime.now());
        tagModel.setName(updateRequest.getName());

        Set<NewsModel> newsModelSet = new HashSet<>();
        for (Long newsId : updateRequest.getNewsId()) {
            newsModelSet.add(newsRepository.findById(newsId).orElseThrow(() -> new NoSuchElementException("No such news")));
        }
        tagModel.setNews(newsModelSet);

        return TagMapper.INSTANCE.tagToTagDTO(repository.save(tagModel));
    }

    @Override
    public boolean deleteById(Long id) {
        TagModel tagModel = repository.findById(id).orElse(null);
        if (tagModel == null) return false;
        else {
            repository.deleteById(id);
            return true;
        }
    }
}
