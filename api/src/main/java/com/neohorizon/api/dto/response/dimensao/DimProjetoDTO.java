package com.neohorizon.api.dto.response.dimensao;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimProjetoDTO {
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID do projeto (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do projeto", example = "Neo Horizon", required = true)
    private String nome;
    
    @Schema(description = "Chave única do projeto", example = "NEOHZ", required = true)
    private String key;
    
    @Schema(description = "ID do projeto no JIRA", example = "JIRA-2001")
    private String jira_id;
}
