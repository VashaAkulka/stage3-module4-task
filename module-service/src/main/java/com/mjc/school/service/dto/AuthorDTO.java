package com.mjc.school.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AuthorDTO {
    @NotNull
    @Size(min = 3, max = 15)
    private String name;
    private Set<Long> newsId;
}
