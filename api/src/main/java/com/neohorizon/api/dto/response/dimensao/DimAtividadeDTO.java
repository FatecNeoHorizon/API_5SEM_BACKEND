package com.neohorizon.api.dto.response.dimensao;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimAtividadeDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID da atividade (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome da atividade", example = "Criar endpoint de autenticação", required = true)
    private String nome;
    
    @Schema(description = "Descrição da atividade", example = "Implementar sistema de login com JWT")
    private String descricao;
    
    @Schema(description = "ID do projeto ao qual a atividade pertence", example = "1", required = true)
    private Long projetoId;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Nome do projeto (apenas leitura)", example = "Neo Horizon")
    private String projetoNome;
    
    @Schema(description = "Se a atividade está ativa", example = "true")
    private Boolean ativo;
}
