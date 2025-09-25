// src/main/java/com/neohorizon/api/dto/EvolucaoCustoDTO.java
package com.neohorizon.api.dto;

public class EvolucaoCustoDTO {
    public String periodo;
    public Long custo;
    public EvolucaoCustoDTO(String periodo, Long custo) {
        this.periodo = periodo;
        this.custo = custo == null ? 0L : custo;
    }
}
