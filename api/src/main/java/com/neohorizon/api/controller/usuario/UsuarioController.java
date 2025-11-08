package com.neohorizon.api.controller.usuario;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neohorizon.api.controller.comum.BaseController;
import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.service.seguranca.UsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/usuario")
public class UsuarioController extends BaseController{

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        List<UsuarioDTO> users = usuarioService.getAllUsers();
        return ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUserById(@PathVariable Long id) {
        UsuarioDTO user = usuarioService.getUserById(id);
        return ok(user);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUser(UsuarioDTO usuarioDTO) {
        UsuarioDTO createdUser = usuarioService.createUser(usuarioDTO);
        return created(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable Long id, UsuarioDTO usuarioDTO) {
        UsuarioDTO updatedUser = usuarioService.updateUser(id, usuarioDTO);
        return ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
        boolean updated = usuarioService.updatePassword(id, newPassword);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
