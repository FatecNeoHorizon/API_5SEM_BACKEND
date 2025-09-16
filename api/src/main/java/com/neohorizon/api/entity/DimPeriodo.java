package com.neohorizon.api.entity;

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

    @NonNull
    @Column(name = "periodo_dia")
    private Integer dia;

    @NonNull
    @Column(name = "periodo_semana")
    private Integer semana;

    @NonNull
    @Column(name = "periodo_mes")
    private Integer mes;

    @NonNull
    @Column(name = "periodo_ano")
    private Integer ano;
}
