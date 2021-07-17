package com.mb.springrest.blogproject.service;

import com.mb.springrest.blogproject.model.Tags;
import com.mb.springrest.blogproject.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tags addNewTags(Tags newTags) {
        return tagRepository.save(newTags);
    }

    public List<Tags> getAllTags() {
        return tagRepository.findAll();
    }

    public Optional<Tags> getById(int tagId) {
        return tagRepository.findById(tagId);
    }

    public void deleteTags(Tags existingTags) {
        tagRepository.delete(existingTags);
    }

    public Tags getTagByName(String tagName) {
        return tagRepository.findByName(tagName);
    }
}
