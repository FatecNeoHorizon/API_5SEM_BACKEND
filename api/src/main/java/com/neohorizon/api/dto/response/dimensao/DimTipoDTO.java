package com.neohorizon.api.dto.response.dimensao;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimTipoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID do tipo (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do tipo", example = "Desenvolvimento", required = true)
    private String nome;
    
    @Schema(description = "Descrição do tipo", example = "Implementação de funcionalidades")
    private String descricao;
}
