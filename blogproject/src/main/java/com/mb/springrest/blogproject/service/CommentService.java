package com.mb.springrest.blogproject.service;

import com.mb.springrest.blogproject.model.Comment;
import com.mb.springrest.blogproject.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment addNewComment(Comment newComment) {
        return commentRepository.save(newComment);
    }

    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findByPostId(postId);
    }

    public Optional<Comment> getById(int commentId) {
        return commentRepository.findById(commentId);
    }

    public void deleteComment(Comment existingComment) {
        commentRepository.delete(existingComment);
    }
}
