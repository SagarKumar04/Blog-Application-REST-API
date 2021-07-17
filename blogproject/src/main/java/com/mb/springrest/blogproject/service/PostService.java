package com.mb.springrest.blogproject.service;

import com.mb.springrest.blogproject.model.Post;
import com.mb.springrest.blogproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post addNewPost(Post newPost) {
        return postRepository.save(newPost);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Post> getById(int postId) {
        return postRepository.findById(postId);
    }

    public void deletePost(Post existingPost) {
        postRepository.delete(existingPost);
    }
}