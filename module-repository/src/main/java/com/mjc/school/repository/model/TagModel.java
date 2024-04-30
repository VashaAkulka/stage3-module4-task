package com.mjc.school.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Table(name = "tags")
@Entity
public class TagModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @ManyToMany(mappedBy = "tags")
    private Set<NewsModel> news = new HashSet<>();
}
