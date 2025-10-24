package com.neohorizon.api.dto.response.fato;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataAtualizacao;
}
