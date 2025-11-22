package com.neohorizon.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neohorizon.api.entity.usuario.Usuario;
import com.neohorizon.api.enums.RoleType;
import com.neohorizon.api.repository.usuario.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataInitializer {

    @Value("${app.etl.email}")
    private String etlEmail;
    
    @Value("${app.etl.password}")
    private String etlPassword;

    @Value("${app.admin.email}")
    private String adminEmail;
    
    @Value("${app.admin.password}")
    private String adminPassword;


    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByEmail(etlEmail).isEmpty()) {
                log.info("Criando usuário técnico para ETL...");
                
                Usuario etlUser = new Usuario();
                etlUser.setEmail(etlEmail);
                etlUser.setSenha(passwordEncoder.encode(etlPassword));
                etlUser.setCargo(RoleType.ETL);
                
                usuarioRepository.save(etlUser);
                log.info("✅ Usuário ETL criado com sucesso: {}", etlEmail);
                log.warn("⚠️ IMPORTANTE: Configure as variáveis ETL_EMAIL e ETL_PASSWORD em produção!");
            } else {
                log.info("Usuário ETL já existe: {}", etlEmail);
            }

            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                log.info("Criando usuário técnico para ADMIN...");
                
                Usuario adminUser = new Usuario();
                adminUser.setEmail(adminEmail);
                adminUser.setSenha(passwordEncoder.encode(adminPassword));
                adminUser.setCargo(RoleType.ADMIN);
                
                usuarioRepository.save(adminUser);
                log.info("✅ Usuário ADMIN criado com sucesso: {}", adminEmail);
                log.warn("⚠️ IMPORTANTE: Configure as variáveis ADMIN_EMAIL e ADMIN_PASSWORD em produção!");
            } else {
                log.info("Usuário ADMIN já existe: {}", adminEmail);
            }

        };
    }
    
}
