package com.mb.springrest.blogproject.controller;

import com.mb.springrest.blogproject.model.Comment;
import com.mb.springrest.blogproject.model.Post;
import com.mb.springrest.blogproject.model.User;
import com.mb.springrest.blogproject.service.CommentService;
import com.mb.springrest.blogproject.service.PostService;
import com.mb.springrest.blogproject.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts/{post_id}/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Comment> allComments(@PathVariable(value = "post_id") int postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{id}")
    public Optional<Comment> commentById(@PathVariable(value = "id") int commentId) {
        return commentService.getById(commentId);
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment, @PathVariable(value = "post_id") int postId) {
        Date date = new Date();

        comment.setCreatedAt(date);
        comment.setUpdatedAt(date);
        comment.setPostId(postId);

        return commentService.addNewComment(comment);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestHeader(value = "Authorization") String header, @RequestBody Comment comment, @PathVariable("post_id") int postId, @PathVariable("id") int commentId) throws Exception {
        String jwt = header.split(" ")[1];
        String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().get("sub", String.class);
        User user = userService.getUserByEmail(email);
        String userName = user.getName();
        String userRole = user.getRoles().split("_")[1];
        Post post = postService.getById(postId).get();
        String authorName = post.getAuthor();
        Comment existingComment = commentService.getById(commentId).get();
        String commenterName = comment.getName();

        if(userRole.equalsIgnoreCase("admin")
        || userName.equalsIgnoreCase(authorName)
        || userName.equalsIgnoreCase(commenterName)) {
            Date date = new Date();

            existingComment.setName(comment.getName());
            existingComment.setEmail(comment.getEmail());
            existingComment.setComment(comment.getComment());
            existingComment.setCreatedAt(existingComment.getCreatedAt());
            existingComment.setUpdatedAt(date);
            existingComment.setPostId(existingComment.getPostId());
        }
        else {
            throw new Exception(userName + " is not allowed to edit comments on " + authorName + "'s post");
        }

        return commentService.addNewComment(existingComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteComment(@RequestHeader(value = "Authorization") String header, @PathVariable("post_id") int postId, @PathVariable("id") int commentId) throws Exception {
        String jwt = header.split(" ")[1];
        String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().get("sub", String.class);
        User user = userService.getUserByEmail(email);
        String userName = user.getName();
        String userRole = user.getRoles().split("_")[1];
        Post post = postService.getById(postId).get();
        String authorName = post.getAuthor();

        Comment comment = commentService.getById(commentId).get();
        String commenterName = comment.getName();
        if(userRole.equalsIgnoreCase("admin")
        || userName.equalsIgnoreCase(authorName)
        || userName.equalsIgnoreCase(commenterName)) {
            commentService.deleteComment(comment);
        }
        else {
            throw new Exception(userName + " is not allowed to delete comments on " + authorName + "'s post");
        }
        return ResponseEntity.ok().build();
    }
}