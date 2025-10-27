package com.neohorizon.api.dto.response.dimensao;

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

    @Schema(description = "ID da atividade (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome da atividade", example = "Criar endpoint de autenticação", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;
    
    @Schema(description = "Descrição da atividade", example = "Implementar sistema de login com JWT")
    private String descricao;

    @Schema(description = "ID da atividade no JIRA", example = "0123456")
    private String atividade_jira_id;
    
    @Schema(description = "Se a atividade está ativa", example = "true")
    private Boolean ativo;
}
