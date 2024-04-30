package com.mjc.school.repository.impl;

import com.mjc.school.repository.model.NewsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsModel, Long> {

}
