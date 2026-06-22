package com.jasper.dungeontrackerbackend.services;

import com.jasper.dungeontrackerbackend.dto.user.LoginUserRequest;
import com.jasper.dungeontrackerbackend.dto.user.RegisterUserRequest;
import com.jasper.dungeontrackerbackend.entities.User;
import com.jasper.dungeontrackerbackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * register a new user from a register user dto
     * @param request the dto containing the user information
     * @return the newly created user
     */
    @Transactional
    public User registerUser(RegisterUserRequest request) {
        // 1. Enforce business rule uniqueness constraints
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // 2. Hash the raw password securely using BCrypt
        String securePasswordHash = passwordEncoder.encode(request.password());

        // 3. Map DTO to Entity and save to PostgreSQL via the repository
        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(securePasswordHash)
                .isActive(true)
                .build();

        return userRepository.save(newUser);
    }

    /**
     * Attempts to find user from provided username/email and password pair.
     * @param request the login request DTO
     * @return If a user is found and the password is correct, return optional object containing the user.
     * If no user is found, returns an empty optional object.
     */
    @Transactional
    public Optional<User> loginUser(LoginUserRequest request) {
        // Allow logging in via either username OR email
        Optional<User> userOpt = userRepository.findByUsername(request.usernameOrEmail());
        if (userOpt.isEmpty()) {
            //if no user found via username, check email
            userOpt = userRepository.findByEmail(request.usernameOrEmail());
        }

        // If user exists, check if the submitted raw password matches the encrypted hash
        if (userOpt.isPresent() && passwordEncoder.matches(request.password(), userOpt.get().getPasswordHash())) {
            return userOpt;
        }

        return Optional.empty(); // Login failed (bad username/email or password)
    }

    /**
     * Delete a user by their uuid
     * @param userId the uuid of the user
     */
    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }


}
