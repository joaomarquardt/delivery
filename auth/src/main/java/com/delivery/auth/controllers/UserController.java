package com.delivery.auth.controllers;

import com.delivery.auth.dtos.requests.CreateUserRequest;
import com.delivery.auth.dtos.requests.UpdateUserRequest;
import com.delivery.auth.dtos.responses.UserResponse;
import com.delivery.auth.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable(value = "id") Long id, @RequestHeader("X-User-Id") Long authenticatedUserId, @RequestHeader("X-User-Role") String userRole) {
        UserResponse user = userService.findUserById(id, authenticatedUserId, userRole);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
        UserResponse createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateUserRequest user, @RequestHeader("X-User-Id") Long userId, @RequestHeader("X-User-Role") String userRole) {
        UserResponse updatedUser = userService.updateUser(id, user, userId, userRole);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id, @RequestHeader("X-User-Id") Long userId, @RequestHeader("X-User-Role") String userRole) {
        userService.deleteUser(id, userId, userRole);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
