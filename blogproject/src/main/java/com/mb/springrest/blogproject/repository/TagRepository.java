package com.mb.springrest.blogproject.repository;

import com.mb.springrest.blogproject.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags, Integer> {
    Tags findByName(String tagName);
}
