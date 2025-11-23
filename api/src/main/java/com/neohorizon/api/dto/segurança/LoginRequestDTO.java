package com.neohorizon.api.dto.segurança;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotNull(message = "Email é obrigatório e não pode estar vazio")
    @NotBlank(message = "Email é obrigatório e não pode estar vazio")
    private String email;

    @NotNull(message = "Senha é obrigatória e não pode estar vazia")
    @NotBlank(message = "Senha é obrigatória e não pode estar vazia")
    private String password;

}
