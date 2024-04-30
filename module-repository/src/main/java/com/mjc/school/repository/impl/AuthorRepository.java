package com.mjc.school.repository.impl;

import com.mjc.school.repository.model.AuthorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorModel, Long> {
    @Query("SELECT a FROM AuthorModel a join a.news n where n.id = :newsId")
    List<AuthorModel> findAuthorByNewsId(@Param("newsId") Long newsId);
}
