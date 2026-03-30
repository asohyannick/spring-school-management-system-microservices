package com.ecol.userService.dto;
import com.ecol.userService.enums.UserRole;
import java.time.Instant;
import java.util.UUID;
public record UserResponseDto(

        UUID id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        boolean accountVerified,
        boolean accountBlocked,
        boolean accountDeleted,
        boolean accountSuspended,
        boolean accountLocked,
        Instant createdAt,
        Instant updatedAt

) {}