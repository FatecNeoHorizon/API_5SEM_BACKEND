package com.neohorizon.api.dto.response.fato;

import java.math.BigDecimal;

import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;

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
    private BigDecimal custo;
    private BigDecimal horasQuantidade;
}
