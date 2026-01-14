package com.delivery.auth.services;

import com.delivery.auth.domain.User;
import com.delivery.auth.dtos.requests.CreateUserRequest;
import com.delivery.auth.dtos.requests.UpdateUserRequest;
import com.delivery.auth.dtos.responses.UserResponse;
import com.delivery.auth.mappers.UserMapper;
import com.delivery.auth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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

    public UserResponse findUserById(Long id, Long authenticatedUserId, String userRole) {
        User user = findUserEntityById(id);
        if (!userRole.equals("ADMIN") && !user.getId().equals(authenticatedUserId)) {
            throw new IllegalArgumentException("You do not have permission to access this user");
        }
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toUserEntity(request);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponse(createdUser);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request, Long userId, String userRole) {
        User user = findUserEntityById(id);
        if (!userRole.equals("ADMIN") && !user.getId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to update this user");
        }
        userMapper.updateUserFromRequest(request, user);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponse(createdUser);
    }

    public void deleteUser(Long id, Long userId, String userRole) {
        User user = findUserEntityById(id);
        if (!userRole.equals("ADMIN") && !user.getId().equals(userId)) {
            throw new IllegalArgumentException("You do not have permission to delete this user");
        }
        userRepository.deleteById(id);
    }
}
