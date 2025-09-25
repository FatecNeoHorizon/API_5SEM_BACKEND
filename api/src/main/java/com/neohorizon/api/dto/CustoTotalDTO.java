
package com.neohorizon.api.dto;

public class CustoTotalDTO {
    public Long total;
    public CustoTotalDTO(Long total) {
        this.total = total == null ? 0L : total;
    }
}
