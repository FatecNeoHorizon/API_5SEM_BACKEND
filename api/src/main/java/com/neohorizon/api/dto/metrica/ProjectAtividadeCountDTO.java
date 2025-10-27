package com.neohorizon.api.dto.metrica;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAtividadeCountDTO {
    private String projectName;
    private BigDecimal totalAtividades;
}