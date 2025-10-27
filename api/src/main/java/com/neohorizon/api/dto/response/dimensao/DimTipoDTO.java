package com.neohorizon.api.dto.response.dimensao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimTipoDTO {
    
    @Schema(description = "ID do tipo (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do tipo", example = "Desenvolvimento", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;
    
    @Schema(description = "Descrição do tipo", example = "Implementação de funcionalidades")
    private String descricao;

    @Schema(description = "ID do tipo no Jira", example = "10001")
    private String tipoJiraId;
}
