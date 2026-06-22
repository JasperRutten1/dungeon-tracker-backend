package com.jasper.dungeontrackerbackend.dto.user;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID userId,
        String username
) {
}
