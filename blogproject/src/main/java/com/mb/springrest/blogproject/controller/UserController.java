package com.mb.springrest.blogproject.controller;

import com.mb.springrest.blogproject.model.User;
import com.mb.springrest.blogproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    //get all users
    @GetMapping
    public List<User> allUsers() {
        return userService.getAllUsers();
    }

    //get user by id
    @GetMapping("/{id}")
    public Optional<User> userById(@PathVariable (value = "id") int userId) {
        return userService.getById(userId);
    }


    //save user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    //update user
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable("id") int userId) {
        User existingUser = userService.getById(userId).get();

        existingUser.setId(user.getId());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setActive(user.getActive());
        existingUser.setRoles(user.getRoles());

        return userService.addNewUser(existingUser);
    }

    //delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int userId) {
        User existingUser = userService.getById(userId).get();

        userService.deleteUser(existingUser);

        return ResponseEntity.ok().build();
    }
}
