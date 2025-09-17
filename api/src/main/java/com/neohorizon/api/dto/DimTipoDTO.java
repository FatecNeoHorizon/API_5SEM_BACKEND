package com.neohorizon.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimTipoDTO {

    private Long id;
    private String nome;
    private String descricao;
}
