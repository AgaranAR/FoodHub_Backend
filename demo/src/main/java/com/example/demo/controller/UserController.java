package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register endpoint
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // Login endpoint
    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        Optional<User> user = userService.loginUser(loginUser.getEmail(), loginUser.getPassword());
        if (user.isPresent()) {
            return "Login successful! Welcome " + user.get().getName() + " (" + user.get().getRole() + ")";
        } else {
            return "Invalid email or password!";
        }
    }
}
