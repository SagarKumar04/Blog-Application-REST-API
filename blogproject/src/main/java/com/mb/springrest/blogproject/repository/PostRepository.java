package com.mb.springrest.blogproject.repository;

import com.mb.springrest.blogproject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
