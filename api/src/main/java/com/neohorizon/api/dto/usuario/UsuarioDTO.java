package com.neohorizon.api.dto.usuario;

import com.neohorizon.api.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String email;
    private String senha;
    private RoleType cargo;

}
