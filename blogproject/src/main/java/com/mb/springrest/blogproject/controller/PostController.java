package com.mb.springrest.blogproject.controller;

import com.mb.springrest.blogproject.model.*;
import com.mb.springrest.blogproject.service.CommentService;
import com.mb.springrest.blogproject.service.PostService;
import com.mb.springrest.blogproject.service.TagService;
import com.mb.springrest.blogproject.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Post> allPosts(@RequestParam(name = "start", required = false) Integer start,
                               @RequestParam(name = "size", required = false) Integer size,
                               @RequestParam(name = "sort", required = false) String sort) {
        if(start != null && size != null && sort != null) {
            Sort.Direction sortDirection = null;

            if(sort.equals("published_date")) {
                sortDirection = Sort.Direction.ASC;
            }
            if(sort.equals("-published_date")) {
                sortDirection = Sort.Direction.DESC;
            }
            Pageable pageable = PageRequest.of(start, size, sortDirection, "createdAt");
            return postService.getAllPosts(pageable).getContent();
        }

        if(start != null && size != null) {
            Pageable pageable = PageRequest.of(start, size, Sort.Direction.DESC, "createdAt");
            return postService.getAllPosts(pageable).getContent();
        }

        return postService.getAllPosts();
    }

    @GetMapping("/search")
    public List<Post> search(@RequestParam(name = "keyword", required = false) String keyword,
                             @RequestParam(name = "author", required = false) String author,
                             @RequestParam(name = "date", required = false) String date,
                             @RequestParam(name = "tag", required = false) String tag) {
        List<Post> allPosts = postService.getAllPosts();
        List<Post> tempPostsList = new ArrayList<>();
        List<Post> searchedAndFilteredPosts = new ArrayList<>();
        List<String> filters = new ArrayList<>();
        List<String> filterList = new ArrayList<>();

        if (keyword != null) {
            for (Post p : allPosts) {
                if (p.getTitle().toLowerCase().contains(keyword) || p.getContent().toLowerCase().contains(keyword)
                        || p.getAuthor().toLowerCase().contains(keyword) || p.getTags().toLowerCase().contains(keyword)) {
                    searchedAndFilteredPosts.add(p);
                }
            }
        }

        filters.add("author");
        filters.add("date");
        filters.add("tag");

        filterList.add(author);
        filterList.add(date);
        filterList.add(tag);

        if (keyword == null) {
            searchedAndFilteredPosts = allPosts;
        }

        for (int i = 0; i < 3; i++) {
            if (filterList.get(i) != null) {
                switch (filters.get(i).charAt(0)) {
                    case 'a':
                        for (Post p : searchedAndFilteredPosts) {
                            if (!(p.getAuthor().toLowerCase().contains(author))) {
                                tempPostsList.add(p);
                            }
                        }
                        searchedAndFilteredPosts.removeAll(tempPostsList);
                        tempPostsList.removeAll(tempPostsList);
                        break;

                    case 'd':
                        for (Post p : searchedAndFilteredPosts) {
                            if (!(p.getCreatedAt().toString().toLowerCase().contains(date))) {
                                tempPostsList.add(p);
                            }
                        }
                        searchedAndFilteredPosts.removeAll(tempPostsList);
                        tempPostsList.removeAll(tempPostsList);
                        break;

                    case 't':
                        for (Post p : searchedAndFilteredPosts) {
                            if (!(p.getTags().toLowerCase().contains(tag))) {
                                tempPostsList.add(p);
                            }
                        }
                        searchedAndFilteredPosts.removeAll(tempPostsList);
                        tempPostsList.removeAll(tempPostsList);
                        break;
                }
            }
        }
        return searchedAndFilteredPosts;
    }

    @GetMapping("/{id}")
    public LinkedHashMap<Post, List<Comment>> postById(@PathVariable(value = "id") int postId) {
        LinkedHashMap<Post, List<Comment>> postAndComment = new LinkedHashMap<>();
        Optional<Post> postFromDatabase = postService.getById(postId);
        Post post = postFromDatabase.get();
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        postAndComment.put(post, comments);

        return postAndComment;
    }

    @PostMapping
    public Post createPost(@RequestHeader(value = "Authorization", required = false) String header, @RequestBody Post post) {
        String userName, userRole;

        if(header != null) {
            String jwt = header.split(" ")[1];
            String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().get("sub", String.class);
            User user = userService.getUserByEmail(email);
            userName = user.getName();
            userRole = user.getRoles().split("_")[1];
        }
        else {
            userName = post.getAuthor();
            userRole = "GUEST";
        }
        String authorName = post.getAuthor();
        String tag[] = post.getTags().split(",");
        int length = tag.length;
        Tags tags[] = new Tags[length];
        PostTags postTags[] = new PostTags[length];
        Date date = new Date();

        if(userRole.equalsIgnoreCase("admin")) {
            post.setAuthor(authorName);
        }
        else {
            post.setAuthor(userName);
        }

        post.setPublishedAt(date);
        post.setCreatedAt(date);
        post.setUpdatedAt(date);

        for(int i = 0; i < length; i++) {
            tags[i] = new Tags(tag[i].trim(), date, date);
        }

        for(int i = 0; i < length; i++) {
            postTags[i] = new PostTags(post, tags[i], date, date);
        }

        for(int i = 0; i < length; i++) {
            post.addPostTags(postTags[i]);
        }

        return postService.addNewPost(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@RequestHeader(value = "Authorization") String header, @RequestBody Post post, @PathVariable("id") int postId) throws Exception {
        String jwt = header.split(" ")[1];
        String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().get("sub", String.class);
        User user = userService.getUserByEmail(email);
        String userName = user.getName();
        String userRole = user.getRoles().split("_")[1];
        String authorName = post.getAuthor();

        if(userRole.equalsIgnoreCase("admin")) {
            authorName = post.getAuthor();
        }
        else {
            if(userName.equalsIgnoreCase(authorName)) {
                authorName = userName;
            }
            else {
                throw new Exception(userName + " is not allowed to edit " + authorName + "'s post");
            }
        }

        Post existingPost = postService.getById(postId).get();
        Date date = new Date();

        existingPost.setId(existingPost.getId());
        existingPost.setTitle(post.getTitle());
        existingPost.setExcerpt(post.getExcerpt());
        existingPost.setContent(post.getContent());
        existingPost.setAuthor(post.getAuthor());
        existingPost.setPublishedAt(existingPost.getPublishedAt());
        existingPost.setIsPublished(post.getIsPublished());
        existingPost.setCreatedAt(existingPost.getCreatedAt());
        existingPost.setUpdatedAt(date);
        existingPost.setTags(post.getTags());

        return postService.addNewPost(existingPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@RequestHeader(value = "Authorization") String header, @PathVariable("id") int postId) throws Exception {
        String jwt = header.split(" ")[1];
        String email = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).getBody().get("sub", String.class);
        User user = userService.getUserByEmail(email);
        String userName = user.getName();
        String userRole = user.getRoles().split("_")[1];
        Post post = postService.getById(postId).get();
        String authorName = post.getAuthor();

        if(userRole.equalsIgnoreCase("admin")) {
            postService.deletePost(post);
        }
        else {
            if(userName.equalsIgnoreCase(authorName)) {
                postService.deletePost(post);
            }
            else {
                throw new Exception(userName + " is not allowed to delete " + authorName + "'s post");
            }
        }
        return ResponseEntity.ok().build();
    }
}