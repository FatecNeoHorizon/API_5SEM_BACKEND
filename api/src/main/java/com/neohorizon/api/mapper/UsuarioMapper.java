package com.neohorizon.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.usuario.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    @Mapping(target = "usuario_id", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
    UsuarioDTO toDTO(Usuario usuario);

    List<Usuario> toEntityList(List<UsuarioDTO> usuarioDTOs);
    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
}
