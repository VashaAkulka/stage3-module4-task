package com.mjc.school.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    @NotNull
    @Size(min = 5, max = 30)
    private String content;
    @NotNull
    private Long newsId;
}
