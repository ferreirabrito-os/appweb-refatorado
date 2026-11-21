package com.application.appweb.dto.user;

import jakarta.validation.constraints.Size;
import java.util.Set;

public record UserUpdateDTO(
        String username,
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String password,
        Set<String> roleNames
) {}
