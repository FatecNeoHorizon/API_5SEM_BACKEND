package com.neohorizon.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimDevDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String role;
    private Integer custo_hora;
}
