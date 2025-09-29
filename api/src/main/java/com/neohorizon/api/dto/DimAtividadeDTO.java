package com.neohorizon.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimAtividadeDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long projetoId;
    private String projetoNome;
    private Boolean ativo;
}
