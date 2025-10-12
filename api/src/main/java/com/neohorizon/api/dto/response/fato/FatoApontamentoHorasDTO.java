package com.neohorizon.api.dto.response.fato;

import java.time.LocalDateTime;

import com.neohorizon.api.dto.response.dimensao.DimAtividadeDTO;
import com.neohorizon.api.dto.response.dimensao.DimDevDTO;
import com.neohorizon.api.dto.response.dimensao.DimPeriodoDTO;
import com.neohorizon.api.dto.response.dimensao.DimProjetoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoApontamentoHorasDTO {

    private Long id;
    private DimPeriodoDTO dimPeriodo;
    private DimDevDTO dimDev;
    private DimAtividadeDTO dimAtividade;
    private DimProjetoDTO dimProjeto;
    private Double horasTrabalhadas;
    private String descricaoTrabalho;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
