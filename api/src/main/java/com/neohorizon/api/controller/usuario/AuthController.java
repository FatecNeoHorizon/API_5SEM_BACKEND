package com.neohorizon.api.controller.usuario;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.dto.segurança.LoginRequestDTO;
import com.neohorizon.api.dto.segurança.TokenResponseDTO;
import com.neohorizon.api.service.usuario.UsuarioService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequestDTO credentials) {
        String token = usuarioService.authenticate(credentials.getEmail(), credentials.getPassword());
        TokenResponseDTO response = new TokenResponseDTO(token, "Bearer", 3600);
        return ResponseEntity.ok(response);
    }

}
