package com.application.appweb.dto.user;

import com.application.appweb.model.User;
import java.util.Set;

public record UserResponseDTO(Long id, String username, Set<String> roles) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet())
        );
    }
}
