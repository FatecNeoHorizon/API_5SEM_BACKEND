package com.neohorizon.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.neohorizon.api.dto.usuario.UsuarioDTO;
import com.neohorizon.api.entity.seguranca.Usuario;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
    UsuarioDTO toDTO(Usuario usuario);

    List<Usuario> toEntityList(List<UsuarioDTO> usuarioDTOs);
    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);

}
