package com.neohorizon.api.dto.response.fato;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Long devId;
    private String devNome;
    private Long atividadeId;
    private String atividadeNome;
    private Long projetoId;
    private String projetoNome;
    private LocalDate dataApontamento;
    private Double horasTrabalhadas;
    private String descricaoTrabalho;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
