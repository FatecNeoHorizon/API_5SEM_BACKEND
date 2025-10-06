package com.neohorizon.api.dto.response.dimensao;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimDevDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID do desenvolvedor (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do desenvolvedor", example = "Alice Souza", required = true)
    private String nome;
    
    @Schema(description = "Email do desenvolvedor", example = "alice@empresa.com")
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Senha do desenvolvedor (apenas escrita)", example = "senha123")
    private String senha;
    
    @Schema(description = "Função/cargo do desenvolvedor", example = "Backend")
    private String role;
    
    @Schema(description = "Custo por hora em reais", example = "120")
    private Integer custo_hora;
    
    @Schema(description = "Se o desenvolvedor está ativo", example = "true")
    private Boolean ativo;
}
