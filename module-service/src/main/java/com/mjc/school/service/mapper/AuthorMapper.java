package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface AuthorMapper {
    AuthorMapper INSTANCE = Mappers.getMapper( AuthorMapper.class );

    @Mapping(target = "newsId", source = "news")
    AuthorDTO authorToAuthorDto(AuthorModel author);

    @Mapping(target = "newsId", source = "news")
    List<AuthorDTO> authorListToAuthorDtoList(List<AuthorModel> authorList);

    default Set<Long> mapNewsToNewsId(Set<NewsModel> news) {
        return news.stream()
                .map(NewsModel::getId)
                .collect(Collectors.toSet());
    }
}
