package com.neohorizon.api.dto.response.dimensao;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimStatusDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID do status (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do status", example = "Em Progresso", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;
    
    @Schema(description = "ID do status no Jira", example = "3")
    private String statusJiraId;
}
