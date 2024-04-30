package com.mjc.school.repository.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "comments")
@Entity
public class CommentModel implements BaseEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "newsId")
    private NewsModel news;
}
