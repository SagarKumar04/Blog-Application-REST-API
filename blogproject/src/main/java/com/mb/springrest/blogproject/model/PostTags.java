package com.mb.springrest.blogproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_tags")
public class PostTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_id")
    private int postTagId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private Tags tag;

    private Date createdAt;

    private Date updatedAt;

    public PostTags() {

    }

    public PostTags(Post post, Tags tag, Date createdAt, Date updatedAt) {
        this.post = post;
        this.tag = tag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPostTagId() {
        return postTagId;
    }

    public void setPostTagId(int postTagId) {
        this.postTagId = postTagId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "PostTags{" +
                "postTagId=" + postTagId +
                ", post=" + post +
                ", tag=" + tag +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}