package com.neohorizon.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimPeriodoDTO {

    private Long id;
    private Integer dia;
    private Integer semana;
    private Integer mes;
    private Integer ano;
}
