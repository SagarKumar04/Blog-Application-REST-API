package com.mb.springrest.blogproject.service;

import com.mb.springrest.blogproject.model.User;
import com.mb.springrest.blogproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    User user;

    public User addNewUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getById(int userId) {
        return userRepository.findById(userId);
    }

    public void deleteUser(User existingUser) {
        userRepository.delete(existingUser);
    }
}
