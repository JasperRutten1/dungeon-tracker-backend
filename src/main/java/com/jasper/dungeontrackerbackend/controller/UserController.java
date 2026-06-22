package com.jasper.dungeontrackerbackend.controller;

import com.jasper.dungeontrackerbackend.dto.user.AuthResponse;
import com.jasper.dungeontrackerbackend.dto.user.LoginUserRequest;
import com.jasper.dungeontrackerbackend.dto.user.RegisterUserRequest;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.services.JWTService;
import com.jasper.dungeontrackerbackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            // Returns 201 Created along with the new user's generated UUID
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            // Returns 400 Bad Request if the username or email is already taken
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest request) {
        Optional<User> userOpt = userService.loginUser(request);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Generate the token for the authenticated user
            String token = jwtService.generateToken(user);

            // Return token + basic user profile info
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username, email, or password");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            // Returns 204 No Content indicating successful deletion
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Returns 404 Not Found if the user ID doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
