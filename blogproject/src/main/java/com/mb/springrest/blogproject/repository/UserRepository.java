package com.mb.springrest.blogproject.repository;

import com.mb.springrest.blogproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}