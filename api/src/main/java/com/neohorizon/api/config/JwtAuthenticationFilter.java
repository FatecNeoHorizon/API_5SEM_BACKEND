package com.neohorizon.api.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Logger jwtLogger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String authHeader = httpRequest.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.replace("Bearer ", "");

                if (!JwtUtils.isTokenExpired(token)) {
                    Authentication authentication = JwtUtils.parseToken(token);
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        jwtLogger.info("Autenticação bem-sucedida para o usuário: {}", authentication.getName());
                    }
                } else {
                    jwtLogger.warn("Token expirado: {}", token);
                }
            }

            chain.doFilter(request, response);
        } catch (IOException | ServletException | RuntimeException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                httpResponse.getWriter().write("Erro de autenticação: " + e.getMessage());
            } catch (IOException ioe) {
                jwtLogger.error("Não foi possível escrever a resposta de erro: {}", ioe.getMessage(), ioe);
            }
            jwtLogger.error("Erro durante a autenticação: {}", e.getMessage(), e);
        }
    }
}
