package com.mjc.school.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class NewsDTO {
    @NotNull
    Long authorId;

    @NotNull
    @Size(min = 5, max = 30)
    String title;

    @NotNull
    @Size(min = 5, max = 255)
    String content;
    Set<Long> tagsId = new HashSet<>();
}
