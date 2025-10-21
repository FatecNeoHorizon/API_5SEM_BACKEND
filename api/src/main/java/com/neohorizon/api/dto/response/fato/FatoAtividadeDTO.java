package com.neohorizon.api.dto.response.fato;

import java.math.BigDecimal;

import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;
import com.neohorizon.api.dto.response.dimensao.DimStatusDTO;
import com.neohorizon.api.dto.response.dimensao.DimTipoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoAtividadeDTO {
    private Long id;
    private DimProjetoDTO dimProjeto;
    private DimPeriodoDTO dimPeriodo;
    private DimStatusDTO dimStatus;
    private DimTipoDTO dimTipo;
    private BigDecimal quantidade;
}