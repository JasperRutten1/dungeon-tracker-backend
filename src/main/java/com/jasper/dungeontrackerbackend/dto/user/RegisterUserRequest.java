package com.jasper.dungeontrackerbackend.dto.user;

public record RegisterUserRequest (
        String username,
        String email,
        String password
){}
