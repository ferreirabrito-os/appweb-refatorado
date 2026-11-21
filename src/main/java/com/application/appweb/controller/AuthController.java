package com.application.appweb.controller;

import com.application.appweb.dto.auth.*;
import com.application.appweb.dto.user.UserResponseDTO;
import com.application.appweb.service.TokenService;
import com.application.appweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody AuthenticationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto, Set.of("ROLE_USER")));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody AuthenticationDTO dto) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(new LoginResponseDTO(tokenService.generateToken(principal)));
    }
}
