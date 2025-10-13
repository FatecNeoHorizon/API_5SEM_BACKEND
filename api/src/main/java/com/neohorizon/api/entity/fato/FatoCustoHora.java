package com.neohorizon.api.entity.fato;

import java.math.BigDecimal;

import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "custo", precision = 10, scale = 2)
    private BigDecimal custo;

    @Column(name = "horas_quantidade", precision = 10, scale = 2)
    private BigDecimal horas_quantidade;
}
