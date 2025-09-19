package com.neohorizon.api.dto;

import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FatoCustoHoraDTO {

    private Long id;
    private DimProjeto dimProjeto;
    private DimPeriodo dimPeriodo;
    private DimDev dimDev;
    private Integer custo;
    private Integer horas_quantidade;
}
