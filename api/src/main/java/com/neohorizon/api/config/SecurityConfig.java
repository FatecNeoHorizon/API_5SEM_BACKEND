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
                .requestMatchers(HttpMethod.GET, "/dim-atividade/").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-dev/").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-periodo/").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-projeto/").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-status/").permitAll()
                .requestMatchers(HttpMethod.GET, "/dim-tipo/").permitAll()
                .requestMatchers(HttpMethod.GET, "/metrics/").permitAll()
                .requestMatchers(HttpMethod.GET, "/fato-atividade/").permitAll()
                .requestMatchers(HttpMethod.GET, "/fato-apontamento-horas/").permitAll()

                // Endpoints de CUSTO - APENAS MANAGER (DEVELOPER não pode acessar)
                .requestMatchers(HttpMethod.GET,"/fato-custo-hora/").hasAnyRole(RoleType.MANAGER.name(), RoleType.ADMIN.name(), RoleType.ETL.name())
                
                // Endpoints de escrita (POST, PUT, DELETE) - apenas Admin (ETL e ADMIN)
                .requestMatchers(HttpMethod.POST, "/dim-/*").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.PUT, "/dim-/*").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.DELETE, "/dim-/*").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())

                .requestMatchers(HttpMethod.POST, "/fato-atividade/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.PUT, "/fato-atividade/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.DELETE, "/fato-atividade/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())

                .requestMatchers(HttpMethod.POST, "/fato-apontamento-horas/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.PUT, "/fato-apontamento-horas/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.DELETE, "/fato-apontamento-horas/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())

                .requestMatchers(HttpMethod.POST, "/fato-custo-hora/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.PUT, "/fato-custo-hora/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                .requestMatchers(HttpMethod.DELETE, "/fato-custo-hora/").hasAnyRole(RoleType.ADMIN.name(), RoleType.ETL.name())
                
                // Qualquer outra rota - apenas MANAGER
                .requestMatchers("/").hasAnyRole(RoleType.ADMIN.name(), RoleType.MANAGER.name(), RoleType.ETL.name())
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
        source.registerCorsConfiguration("/", config);
        return source;
    }



}
