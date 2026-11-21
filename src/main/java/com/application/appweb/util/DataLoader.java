package com.application.appweb.util;

import com.application.appweb.enumModel.Role;
import com.application.appweb.model.User;
import com.application.appweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    private final String username;
    private final String password;

    public DataLoader(
            UserRepository repository,
            PasswordEncoder encoder,
            @Value("${api.security.admin.username:admin}") String username,
            @Value("${api.security.admin.password:Admin@123}") String password) {

        this.repository = repository;
        this.encoder = encoder;
        this.username = username;
        this.password = password;
    }


    @Override
    public void run(String... args) {

        User admin = repository.findByUsername(username)
                .orElseGet(User::new);

        admin.setUsername(username);

        if (admin.getId() == null) {
            admin.setPassword(encoder.encode(password));
        }

        admin.setRoles(Set.of(Role.ROLE_ADMIN));

        repository.save(admin);
    }
}