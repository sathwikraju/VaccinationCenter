package com.vc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vc.entity.User;
import com.vc.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Save a new user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Retrieve a user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Add more methods for user-related operations as needed
}
