package com.neohorizon.api.service.usuario;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neohorizon.api.config.JwtUtils;
import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.usuario.Usuario;
import com.neohorizon.api.exception.EntityNotFoundException;
import com.neohorizon.api.mapper.UsuarioMapper;
import com.neohorizon.api.repository.usuario.UsuarioRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class UsuarioService {

    private static final String ENTITY_NAME = "usuario";
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDTO> getAllUsers() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toDTOList(usuarios);
    }
    
    public UsuarioDTO getUserById(Long usuarioId) {
        ValidationUtils.requireValidId(usuarioId, ENTITY_NAME);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, usuarioId));

        return usuarioMapper.toDTO(usuario);
    }

    public UsuarioDTO createUser(UsuarioDTO usuarioDTO) {
        ValidationUtils.requireValidRole(usuarioDTO.getCargo());
        ValidationUtils.requireNonEmpty(usuarioDTO.getEmail(), "Email do " + ENTITY_NAME);
        ValidationUtils.requireNonEmpty(usuarioDTO.getEmail(), ENTITY_NAME);
        ValidationUtils.requireNonEmpty(usuarioDTO.getSenha(), "Senha do " + ENTITY_NAME);
        ValidationUtils.requireNonNull(usuarioDTO.getCargo(), "Cargo");
        ValidationUtils.requireValidRole(usuarioDTO.getCargo());

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(savedUsuario);
    }

    public UsuarioDTO updateUser(Long usuarioId, UsuarioDTO usuarioDTO) {
        ValidationUtils.requireValidId(usuarioId, ENTITY_NAME);
        ValidationUtils.requireNonEmpty(usuarioDTO.getEmail(), "Email do " + ENTITY_NAME);
        ValidationUtils.requireValidEmail(usuarioDTO.getEmail());
        ValidationUtils.requireNonEmpty(usuarioDTO.getSenha(), "Senha do " + ENTITY_NAME);
        ValidationUtils.requireNonNull(usuarioDTO.getCargo(), "Cargo");
        ValidationUtils.requireValidRole(usuarioDTO.getCargo());

        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, usuarioId));
        
        Usuario usuarioToSave = usuarioMapper.toEntity(usuarioDTO);
        usuarioToSave.setUsuario_id(usuarioId);

        Usuario updatedUsuario = usuarioRepository.save(usuarioToSave);
        return usuarioMapper.toDTO(updatedUsuario);
    }

    public boolean updatePassword(Long id, String newPassword) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonEmpty(newPassword, "Nova senha");

        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingUsuario.setSenha(newPassword);
        usuarioRepository.save(existingUsuario);
        return true;
    }

    public void deleteUser(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
        if (existingUsuario != null) {
            usuarioRepository.delete(existingUsuario);
        }
    }

    public Usuario findById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));
    }

    public Usuario save(Usuario usuario) {
        ValidationUtils.requireNonNull(usuario, ENTITY_NAME);
        return usuarioRepository.save(usuario);
    }

     public UserDetails loadUserById(Long id) throws UsernameNotFoundException {

        ValidationUtils.requireValidId(id, ENTITY_NAME);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAuthorities() != null ? usuario.getAuthorities() :
                new ArrayList<>()
        );
    }

    public String authenticate(String email, String rawPassword) {
        ValidationUtils.requireNonEmpty(email, ENTITY_NAME);
        ValidationUtils.requireNonEmpty(rawPassword, "Senha");

        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (!passwordEncoder.matches(rawPassword, usuario.getSenha())) {
            throw new UsernameNotFoundException("Credenciais inválidas");
        }


        UserDetails principal = new User(
            usuario.getEmail(), usuario.getSenha(), usuario.getAuthorities());
        try {
            return JwtUtils.generateToken(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal.getUsername(), null, principal.getAuthorities()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

}
