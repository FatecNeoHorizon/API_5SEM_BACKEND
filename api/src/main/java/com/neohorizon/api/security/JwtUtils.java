package com.neohorizon.api.security;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private static final String ROLE_PREFIX = "ROLE_";

    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private JwtUtils() {

    }

    public static String generateToken(Authentication usuario) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Login withoutPassword = new Login();
        withoutPassword.setEmail(usuario.getName());

        if (!usuario.getAuthorities().isEmpty()) {
            // Dentro do generateToken()
            List<String> authorities = usuario.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replaceFirst("^ROLE_", "")) // remove prefixo se existir
                .toList();
            withoutPassword.setRoleTypes(authorities);

        }

        String userJson = mapper.writeValueAsString(withoutPassword);
        Date now = new Date();

        return Jwts.builder().claim("UserDetails", userJson)
                .setIssuer("br.com.neohorizon")
                .setSubject(usuario.getName())
                .setExpiration(new Date(now.getTime() + 1000L * 60L * 60L))
                .signWith(KEY, SignatureAlgorithm.HS512).compact();
    }


    public static Authentication parseToken(String token) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String credentialsJson = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("UserDetails", String.class);

        Login usuario = mapper.readValue(credentialsJson, Login.class);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (usuario.getAuthorities() != null) {
            logger.debug("Roles do token: {}", usuario.getAuthorities());

            authorities = usuario.getAuthorities().stream()
                    .map(role -> {
                        // Garante que todas as roles tenham o prefixo ROLE_
                        String normalized = role.startsWith(ROLE_PREFIX)
                                ? role
                                : ROLE_PREFIX + role;
                        return new SimpleGrantedAuthority(normalized);
                    })
                    .toList();
        }

        logger.debug("Autoridades convertidas: {}", authorities);

        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                null,
                authorities
        );
    }


    public static boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return expiration.before(new Date());
        } catch (Exception e) {
            logger.warn("Erro ao validar token JWT: {}", e.getMessage());
            return true;
        }

    }

}
