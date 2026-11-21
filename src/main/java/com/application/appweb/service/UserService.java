package com.application.appweb.service;

import com.application.appweb.dto.auth.AuthenticationDTO;
import com.application.appweb.dto.user.*;
import com.application.appweb.enumModel.Role;
import com.application.appweb.exception.ApiException;
import com.application.appweb.model.User;
import com.application.appweb.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream().map(UserResponseDTO::fromEntity).toList();
    }

    public UserResponseDTO findById(Long id) {
        return UserResponseDTO.fromEntity(getEntity(id));
    }

    @Transactional
    public UserResponseDTO create(AuthenticationDTO dto, Set<String> roles) {
        String username = dto.username().trim();
        if (repository.findByUsername(username).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Nome de usuário já existe");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(dto.password()));
        user.setRoles(parseRoles(roles, Set.of(Role.ROLE_USER)));
        return UserResponseDTO.fromEntity(repository.save(user));
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = getEntity(id);

        if (dto.username() != null && !dto.username().isBlank()
                && !dto.username().equalsIgnoreCase(user.getUsername())) {
            repository.findByUsername(dto.username().trim()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new ApiException(HttpStatus.CONFLICT, "Nome de usuário já existe");
                }
            });
            user.setUsername(dto.username().trim());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(encoder.encode(dto.password()));
        }

        if (dto.roleNames() != null) {
            user.setRoles(parseRoles(dto.roleNames(), Set.of(Role.ROLE_USER)));
        }

        return UserResponseDTO.fromEntity(repository.save(user));
    }

    @Transactional
    public UserResponseDTO updateRoles(Long id, Set<String> roles) {
        User user = getEntity(id);
        user.setRoles(parseRoles(roles, Set.of(Role.ROLE_USER)));
        return UserResponseDTO.fromEntity(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + id));
    }

    private Set<Role> parseRoles(Set<String> names, Set<Role> defaults) {
        if (names == null || names.isEmpty()) return new HashSet<>(defaults);

        try {
            return names.stream()
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(s -> s.toUpperCase().startsWith("ROLE_") ? s.toUpperCase() : "ROLE_" + s.toUpperCase())
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
        } catch (IllegalArgumentException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Uma ou mais roles são inválidas");
        }
    }
}
