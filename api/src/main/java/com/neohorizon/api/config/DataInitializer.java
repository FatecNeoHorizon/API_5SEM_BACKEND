package com.neohorizon.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.neohorizon.api.entity.seguranca.Usuario;
import com.neohorizon.api.enums.RoleType;
import com.neohorizon.api.repository.seguranca.UsuarioRepository;

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

    @Value("${app.dev.email}")
    private String devEmail;
    
    @Value("${app.dev.password}")
    private String devPassword;

    @Value("${app.manager.email}")
    private String managerEmail;
    
    @Value("${app.manager.password}")
    private String managerPassword;

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se o usuário ETL já existe
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

            if (usuarioRepository.findByEmail(devEmail).isEmpty()) {
                log.info("Criando usuário técnico para DEV...");
                
                Usuario devUser = new Usuario();
                devUser.setEmail(devEmail);
                devUser.setSenha(passwordEncoder.encode(devPassword));
                devUser.setCargo(RoleType.DEVELOPER);
                
                usuarioRepository.save(devUser);
                log.info("✅ Usuário DEV criado com sucesso: {}", devEmail);
                log.warn("⚠️ IMPORTANTE: Configure as variáveis DEV_EMAIL e DEV_PASSWORD em produção!");
            } else {
                log.info("Usuário DEV já existe: {}", devEmail);
            }

            if (usuarioRepository.findByEmail(managerEmail).isEmpty()) {
                log.info("Criando usuário técnico para MANAGER...");
                
                Usuario managerUser = new Usuario();
                managerUser.setEmail(managerEmail);
                managerUser.setSenha(passwordEncoder.encode(managerPassword));
                managerUser.setCargo(RoleType.MANAGER);
                
                usuarioRepository.save(managerUser);
                log.info("✅ Usuário MANAGER criado com sucesso: {}", managerEmail);
                log.warn("⚠️ IMPORTANTE: Configure as variáveis MANAGER_EMAIL e MANAGER_PASSWORD em produção!");
            } else {
                log.info("Usuário MANAGER já existe: {}", managerEmail);
            }
        };
    }
}
