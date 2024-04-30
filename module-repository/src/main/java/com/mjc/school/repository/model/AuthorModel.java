package com.mjc.school.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "authors")
@Entity
public class AuthorModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @OneToMany(mappedBy = "author")
    private Set<NewsModel> news = new HashSet<>();
}
