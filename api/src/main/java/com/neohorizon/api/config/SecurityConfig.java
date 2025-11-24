package com.neohorizon.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.neohorizon.api.repository.usuario.UsuarioRepository;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                // Login - Todos
                .requestMatchers(HttpMethod.POST, "/login").permitAll()

                // Swagger - Todos
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                // ========== DEVELOPER - Acesso permitido ==========
                // Atividades
                .requestMatchers(HttpMethod.GET, "/fato-atividade/total").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-atividade/por-projeto").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-atividade/agregado").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                
                // Custos - Leitura
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora/filter").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                
                // Apontamento de horas
                .requestMatchers(HttpMethod.GET, "/fato-apontamento-horas").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                
                // Dimensões
                .requestMatchers(HttpMethod.GET, "/dim-dev").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/dim-projeto").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")
                
                // Métricas
                .requestMatchers(HttpMethod.GET, "/metrics/dev-hours").hasAnyRole("DEVELOPER", "MANAGER", "ADMIN", "ETL")

                // ========== MANAGER/ADMIN/ETL - Custos (Gerenciamento) ==========
                // Custos - Todos os endpoints
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora/total").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora/total-por-projeto").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora/por-dev").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.GET, "/fato-custo-hora/evolucao").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.POST, "/fato-custo-hora", "/fato-custo-hora/**").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/fato-custo-hora", "/fato-custo-hora/**").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/fato-custo-hora", "/fato-custo-hora/**").hasAnyRole("MANAGER", "ADMIN", "ETL")

                // ========== MANAGER/ADMIN/ETL - Gerenciamento de Configurações ==========
                // Usuários
                .requestMatchers(HttpMethod.GET, "/usuarios").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.POST, "/usuarios").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/usuarios", "/usuarios/**").hasAnyRole("MANAGER", "ADMIN", "ETL")
                
                // Desenvolvedores
                .requestMatchers(HttpMethod.GET, "/dev").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.POST, "/dev").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dev", "/dev/**").hasAnyRole("MANAGER", "ADMIN", "ETL")
                
                // Fatos
                .requestMatchers(HttpMethod.GET, "/fato").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.POST, "/fato").hasAnyRole("MANAGER", "ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/fato", "/fato/**").hasAnyRole("MANAGER", "ADMIN", "ETL")

                // ========== ADMIN/ETL - Acesso total (escrita em dimensões e fatos) ==========
                // Dimensões - Escrita
                .requestMatchers(HttpMethod.POST, "/dim-atividade", "/dim-atividade/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-atividade", "/dim-atividade/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-atividade", "/dim-atividade/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/dim-dev", "/dim-dev/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-dev", "/dim-dev/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-dev", "/dim-dev/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/dim-periodo", "/dim-periodo/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-periodo", "/dim-periodo/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-periodo", "/dim-periodo/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/dim-projeto", "/dim-projeto/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-projeto", "/dim-projeto/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-projeto", "/dim-projeto/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/dim-status", "/dim-status/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-status", "/dim-status/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-status", "/dim-status/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/dim-tipo", "/dim-tipo/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/dim-tipo", "/dim-tipo/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/dim-tipo", "/dim-tipo/**").hasAnyRole("ADMIN", "ETL")

                // Fatos - Escrita
                .requestMatchers(HttpMethod.POST, "/fato-atividade", "/fato-atividade/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/fato-atividade", "/fato-atividade/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/fato-atividade", "/fato-atividade/**").hasAnyRole("ADMIN", "ETL")
                
                .requestMatchers(HttpMethod.POST, "/fato-apontamento-horas", "/fato-apontamento-horas/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/fato-apontamento-horas", "/fato-apontamento-horas/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/fato-apontamento-horas", "/fato-apontamento-horas/**").hasAnyRole("ADMIN", "ETL")

                // Métricas - Escrita
                .requestMatchers(HttpMethod.POST, "/metrics", "/metrics/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.PUT, "/metrics", "/metrics/**").hasAnyRole("ADMIN", "ETL")
                .requestMatchers(HttpMethod.DELETE, "/metrics", "/metrics/**").hasAnyRole("ADMIN", "ETL")
                
                // Qualquer outra rota - apenas ADMIN e ETL
                .requestMatchers("/**").hasAnyRole("ADMIN", "ETL")
                .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }



}
