package com.neohorizon.api.entity.dimensao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dim_periodo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimPeriodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "periodo_id")
    private Long id;

    @Column(name = "periodo_dia")
    private Integer dia;

    @Column(name = "periodo_semana")
    private Integer semana;

    @Column(name = "periodo_mes")
    private Integer mes;

    @Column(name = "periodo_ano")
    private Integer ano;
}
