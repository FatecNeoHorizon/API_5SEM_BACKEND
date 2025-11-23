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

import com.neohorizon.api.enums.RoleType;
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
                
                .requestMatchers(HttpMethod.POST, "/login").permitAll()

                // Endpoints de leitura - ADMIN, DEVELOPER, MANAGER e ETL
                .requestMatchers(HttpMethod.GET, "/dim-atividade", "/dim-atividade/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-dev", "/dim-dev/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-periodo", "/dim-periodo/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-projeto", "/dim-projeto/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-status", "/dim-status/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-tipo", "/dim-tipo/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/metrics", "/metrics/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/fato-atividade", "/fato-atividade/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/fato-apontamento-horas", "/fato-apontamento-horas/**").permitAll()
                
                // Endpoints de CUSTO - APENAS MANAGER (DEVELOPER não pode acessar)
                .requestMatchers(HttpMethod.GET,"/fato-custo-hora", "/fato-custo-hora/**").hasAnyRole(RoleType.MANAGER.name(), RoleType.ADMIN.name(), RoleType.ETL.name())
                          
                // Qualquer outra rota - apenas MANAGER
                .requestMatchers("/**").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.realmName("Neo Horizon"))
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
