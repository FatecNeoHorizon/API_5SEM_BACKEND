package com.neohorizon.api.dto.response.dimensao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimPeriodoDTO {

    @Schema(description = "ID do período (apenas leitura)", example = "1")
    private Long id;
    
    @Schema(description = "Dia do período", example = "15")
    private Integer dia;
    
    @Schema(description = "Semana do período", example = "3")
    private Integer semana;
    
    @Schema(description = "Mês do período", example = "1")
    private Integer mes;
    
    @Schema(description = "Ano do período", example = "2024")
    private Integer ano;
}
