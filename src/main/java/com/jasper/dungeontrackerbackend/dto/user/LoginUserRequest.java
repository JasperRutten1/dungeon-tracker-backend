package com.jasper.dungeontrackerbackend.dto.user;

public record LoginUserRequest(
        String usernameOrEmail,
        String password
) {
}
