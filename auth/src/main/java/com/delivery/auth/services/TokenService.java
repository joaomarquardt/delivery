package com.delivery.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.delivery.auth.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String role = user.getUserRole().name();
            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(user.getUsername())
                    .withClaim("idUser", user.getId())
                    .withClaim("name", user.getName().split(" ")[0])
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .withClaim("roles", role)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JWTCreationException("Error while generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    public Instant getExpiration(String token) {
        return JWT.decode(token)
                .getExpiresAtAsInstant();
    }
}
