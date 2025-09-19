package com.neohorizon.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimProjetoDTO {
    
    private Long id;
    private String nome;
    private String key;
    private String jira_id;
}
