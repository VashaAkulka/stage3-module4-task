package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.CommentModel;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.service.dto.CommentDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class );

    @Mapping(target = "newsId", source = "news")
    CommentDTO commentToCommentDTO (CommentModel comment);

    @Mapping(target = "newsId", source = "news")
    List<CommentDTO> commentListToCommentDTOList (List<CommentModel> commentList);

    default Long mapNewsToNewsId(NewsModel news) {
        return news.getId();
    }
}
