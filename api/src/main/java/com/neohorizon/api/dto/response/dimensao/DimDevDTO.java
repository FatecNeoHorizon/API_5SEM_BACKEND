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
    
    @Schema(description = "Nome do desenvolvedor", example = "Alice Souza", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;
      
    @Schema(description = "Custo por hora em reais", example = "120.00")
    private java.math.BigDecimal custo_hora;
}
