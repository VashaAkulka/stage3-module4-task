package com.mjc.school.repository.impl;

import com.mjc.school.repository.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Long> {
    @Query("SELECT c FROM CommentModel c join c.news n where n.id = :newsId")
    List<CommentModel> findCommentByNewsId(@Param("newsId") Long newsId);
}
