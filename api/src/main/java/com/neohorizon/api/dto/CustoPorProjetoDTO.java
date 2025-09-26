package com.neohorizon.api.dto;

public class CustoPorProjetoDTO {
    public Long projetoId;
    public String projetoNome;
    public Long total;
    public CustoPorProjetoDTO(Long projetoId, String projetoNome, Long total) {
        this.projetoId = projetoId;
        this.projetoNome = projetoNome;
        this.total = total == null ? 0L : total;
    }
}
