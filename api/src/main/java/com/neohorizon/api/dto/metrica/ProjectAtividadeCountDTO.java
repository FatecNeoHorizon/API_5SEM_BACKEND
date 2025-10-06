package com.neohorizon.api.dto.metrica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAtividadeCountDTO {
    private String projectName;
    private long totalAtividades;
}