package com.jasper.dungeontrackerbackend.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JWTService {
    private final Algorithm algorithm;
    private final long expirationMs;
    private final JWTVerifier verifier;
    private final UserRepository userRepository;

    public JWTService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs,
            UserRepository userRepository) {
        this.algorithm = Algorithm.HMAC256(secret); // Cryptographic signing algorithm
        this.expirationMs = expirationMs;
        this.verifier = JWT.require(algorithm).build();
        this.userRepository = userRepository;
    }

    /**
     * Generates a signed token for a successfully authenticated user.
     */
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString()) // Store the User's UUID as the subject
                .withClaim("username", user.getUsername()) // Inject custom public claims
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(algorithm);
    }

    /**
     * Validates a token and returns the User's UUID hidden inside it.
     * Throws an exception if the token is altered or expired.
     */
    public UUID extractUserId(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return UUID.fromString(decodedJWT.getSubject());
    }

    /**
     * Get the user from the JWT token if it exists.
     * @param token the JWT token
     * @return the optional user.
     */
    public Optional<User> extractUser(String token) {
        try{
            return userRepository.findById(extractUserId(token));
        }
        catch (JWTVerificationException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
