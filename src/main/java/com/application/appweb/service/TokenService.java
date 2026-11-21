package com.application.appweb.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService {

    private static final String ISSUER = "appweb-api";

    private final String secret;

    public TokenService(
            @Value("${api.security.token.secret}") String secret) {

        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException(
                    "api.security.token.secret deve possuir pelo menos 32 caracteres"
            );
        }

        this.secret = secret;
    }

    public String generateToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            List<String> roles = user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withClaim("roles", roles)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(
                            Instant.now().plus(2, ChronoUnit.HOURS)
                    )
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new IllegalStateException(
                    "Erro ao gerar token JWT", ex
            );
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException ex) {
            return null;
        }
    }
}