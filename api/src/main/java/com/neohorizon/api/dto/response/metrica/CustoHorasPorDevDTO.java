package com.neohorizon.api.dto.response.metrica;

public class CustoHorasPorDevDTO {
    public Long devId;
    public String devNome;
    public Long horas;
    public Long custo;
    public CustoHorasPorDevDTO(Long devId, String devNome, Long horas, Long custo) {
        this.devId = devId;
        this.devNome = devNome;
        this.horas = horas == null ? 0L : horas;
        this.custo = custo == null ? 0L : custo;
    }
}
