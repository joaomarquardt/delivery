package com.delivery.auth.services;

import com.delivery.auth.domain.User;
import com.delivery.auth.dtos.requests.CreateUserRequest;
import com.delivery.auth.dtos.requests.UpdateUserRequest;
import com.delivery.auth.dtos.responses.UserResponse;
import com.delivery.auth.mappers.UserMapper;
import com.delivery.auth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserResponseList(users);
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public User findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponse findUserById(Long id) {
        User user = findUserEntityById(id);
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toUserEntity(request);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponse(createdUser);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUserEntityById(id);
        userMapper.updateUserFromRequest(request, user);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponse(createdUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
