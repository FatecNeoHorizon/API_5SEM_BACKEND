package com.neohorizon.api.entity.fato;

import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fato_custo_hora")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoCustoHora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custo_hora_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @ManyToOne
    @JoinColumn(name = "periodo_id", nullable = false)
    private DimPeriodo dimPeriodo;

    @ManyToOne
    @JoinColumn(name = "dev_id", nullable = false)
    private DimDev dimDev;

    @Column(name = "custo")
    private Integer custo;

    @Column(name = "horas_quantidade")
    private Integer horas_quantidade;
}
