package com.neohorizon.api.mapper;

import java.util.List;

import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.seguranca.Usuario;

public interface UserMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);
    UsuarioDTO toDTO(Usuario usuario);

    List<Usuario> toEntityList(List<UsuarioDTO> usuarioDTOs);
    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);

}
