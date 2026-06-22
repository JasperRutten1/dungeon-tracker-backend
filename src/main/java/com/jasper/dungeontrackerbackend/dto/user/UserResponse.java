package com.jasper.dungeontrackerbackend.dto.user;

import com.jasper.dungeontrackerbackend.entities.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email
) {
    public static UserResponse from(User user){
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
