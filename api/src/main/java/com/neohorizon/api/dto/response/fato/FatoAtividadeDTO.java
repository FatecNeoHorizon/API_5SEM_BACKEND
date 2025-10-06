package com.neohorizon.api.dto.response.fato;

import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.entity.dimensao.DimTipo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FatoAtividadeDTO {

    private Long id;
    private DimProjeto dimProjeto;
    private DimPeriodo dimPeriodo;
    private DimStatus dimStatus;
    private DimTipo dimTipo;
    private Integer quantidade;
}