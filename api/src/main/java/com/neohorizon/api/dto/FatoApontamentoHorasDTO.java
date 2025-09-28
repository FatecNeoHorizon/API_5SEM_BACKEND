package com.neohorizon.api.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
