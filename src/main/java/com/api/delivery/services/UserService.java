package com.api.delivery.services;

import com.api.delivery.domain.User;
import com.api.delivery.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public User createUser(User user) {
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    public User updateUser(Long id, User user) {
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
