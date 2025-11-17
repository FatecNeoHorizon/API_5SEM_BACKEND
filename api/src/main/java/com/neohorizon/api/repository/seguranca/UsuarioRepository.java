package com.neohorizon.api.repository.seguranca;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neohorizon.api.entity.seguranca.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);
}
