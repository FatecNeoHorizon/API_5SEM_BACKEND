package com.neohorizon.api.service.seguranca;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.neohorizon.api.constants.MessageConstants;
import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.seguranca.Usuario;
import com.neohorizon.api.mapper.UserMapper;
import com.neohorizon.api.repository.seguranca.UsuarioRepository;
import com.neohorizon.api.utils.ValidationUtils;

@Service
public class UsuarioService {

    private static final String ENTITY_NAME = "Usuario";
    private final UsuarioRepository usuarioRepository;
    private final UserMapper userMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UserMapper userMapper) {
        this.usuarioRepository = usuarioRepository;
        this.userMapper = userMapper;
    }

    public List<UsuarioDTO> getAllUsers() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return userMapper.toDTOList(usuarios);
    }

    public UsuarioDTO getUserById(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ENTITY_NAME + MessageConstants.USER_PREFIX + id));
        return userMapper.toDTO(usuario);
    }

    public UsuarioDTO createUser(UsuarioDTO usuarioDTO) {
        Validate.notNull(usuarioDTO, ENTITY_NAME + " é obrigatório");
        
        Usuario usuario = userMapper.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return userMapper.toDTO(savedUsuario);
    }

    public UsuarioDTO updateUser(Long id, UsuarioDTO usuarioDTO) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        Validate.notNull(usuarioDTO, ENTITY_NAME + " é obrigatório para atualização");

        Usuario existingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(ENTITY_NAME + MessageConstants.USER_PREFIX + id));
        

        existingUsuario.setEmail(usuarioDTO.getEmail());
        existingUsuario.setSenha(usuarioDTO.getSenha());
        existingUsuario.setCargo(usuarioDTO.getCargo());

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return userMapper.toDTO(updatedUsuario);
    }

    public void deleteUser(Long id) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);

        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ENTITY_NAME + MessageConstants.USER_PREFIX + id));
        usuarioRepository.delete(existingUsuario);
    }

    public boolean updatePassword(Long id, String newPassword) {
        ValidationUtils.requireValidId(id, ENTITY_NAME);
        ValidationUtils.requireNonEmpty(newPassword, "Nova senha");

        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ENTITY_NAME + MessageConstants.USER_PREFIX + id));

        existingUsuario.setSenha(newPassword);
        usuarioRepository.save(existingUsuario);
        return true;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + id));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAuthorities() != null ? usuario.getAuthorities() :
                new ArrayList<>()
        );
    }

}
