package com.neohorizon.api.dto.usuario;

import com.neohorizon.api.enums.RoleType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String email;
    private String senha;
    private RoleType cargo;

}
