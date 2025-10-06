package com.neohorizon.api.dto.metrica;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevHoursMetricsDTO {

    private Long devId;
    private String devNome;
    private String devEmail;
    private Double totalHoras;
    private List<AtividadeHorasDTO> atividades;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AtividadeHorasDTO {
        private Long atividadeId;
        private String atividadeNome;
        private String projetoNome;
        private Double totalHoras;
        private List<DiaHorasDTO> diasApontamentos;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaHorasDTO {
        private LocalDate data;
        private Double horas;
        private String descricaoTrabalho;
    }
}