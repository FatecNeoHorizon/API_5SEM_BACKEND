package com.neohorizon.api.dto;

import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.entity.DimStatus;
import com.neohorizon.api.entity.DimTipo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FatoIssueDTO {

    private Long id;
    private DimProjeto dimProjeto;
    private DimPeriodo dimPeriodo;
    private DimStatus dimStatus;
    private DimTipo dimTipo;
    private Integer quantidade;
}
