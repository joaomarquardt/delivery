package com.demo.delivery.controllers;

import com.demo.delivery.dtos.LoginRequestDTO;
import com.demo.delivery.dtos.LoginResponseDTO;
import com.demo.delivery.dtos.RegisterRequestDTO;
import com.demo.delivery.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDTO dto) {
        authenticationService.register(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        System.out.println("Entrou aqui");
        String token = authenticationService.login(dto);
        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }
}
