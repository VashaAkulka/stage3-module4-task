package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.NewsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper( NewsMapper.class );
    @Mapping(target = "tagsId", source = "tags")
    @Mapping(target = "authorId", source = "author")
    NewsDTO newsToNewsDto(NewsModel news);

    @Mapping(target = "tagsId", source = "tags")
    @Mapping(target = "authorId", source = "author")
    List<NewsDTO> newsListToNewsDtoList(List<NewsModel> newsList);

    default Set<Long> mapTagsToTagsId(Set<TagModel> tags) {
        return tags.stream()
                .map(TagModel::getId)
                .collect(Collectors.toSet());
    }

    default Long mapAuthorToAuthorId(AuthorModel author) {
        return author.getId();
    }
}
