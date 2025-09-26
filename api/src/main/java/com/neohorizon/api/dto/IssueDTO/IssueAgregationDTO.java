package com.neohorizon.api.dto.IssueDTO;

import com.neohorizon.api.enums.AggregationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueAgregationDTO {

    private Integer diaInicio;
    private Integer mesInicio;
    private Integer anoInicio;
    private Integer diaFim;
    private Integer mesFim;
    private Integer anoFim;
    private AggregationType periodo;

}
