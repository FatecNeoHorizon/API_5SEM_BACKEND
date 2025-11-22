package com.neohorizon.api.service.usuario;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
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
        
        // 2. Mapeamento: cria uma entidade a partir do DTO e preserva o id existente
        Usuario usuarioToSave = usuarioMapper.toEntity(usuarioDTO);
        usuarioToSave.setUsuario_id(usuarioId);

        Usuario updatedUsuario = usuarioRepository.save(usuarioToSave);
        return usuarioMapper.toDTO(updatedUsuario);
    }

    public boolean updatePassword(Long id, String newPassword) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonEmpty(newPassword, "Nova senha");

        // Lançando EntityNotFoundException (Status 404)
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        existingUsuario.setSenha(newPassword);
        usuarioRepository.save(existingUsuario);
        return true;
    }

    public void deleteUser(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);

        // Lançando EntityNotFoundException (Status 404)
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forId(ENTITY_NAME, id));

        usuarioRepository.delete(existingUsuario);
    }

}
