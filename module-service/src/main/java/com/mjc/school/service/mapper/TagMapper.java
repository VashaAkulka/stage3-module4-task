package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.model.TagModel;
import com.mjc.school.service.dto.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper( TagMapper.class );

    @Mapping(target = "newsId", source = "news")
    TagDTO tagToTagDTO(TagModel tag);

    @Mapping(target = "newsId", source = "news")
    List<TagDTO> tagListToTagDTOList(List<TagModel> tagList);

    default Set<Long> mapNewsToNewsId(Set<NewsModel> news) {
        return news.stream()
                .map(NewsModel::getId)
                .collect(Collectors.toSet());
    }

}
