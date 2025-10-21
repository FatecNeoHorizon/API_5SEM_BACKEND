package com.neohorizon.api.entity.fato;

import java.math.BigDecimal;

import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimStatus;
import com.neohorizon.api.entity.dimensao.DimTipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fato_atividade",
    uniqueConstraints = @UniqueConstraint(name = "uq_atividade_gran", columnNames = {"atividade_id","projeto_id","periodo_id","status_id","tipo_id"}),
    indexes = {@Index(name = "idx_fato_atividade_proj_periodo", columnList = "projeto_id, periodo_id")}
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoAtividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fato_atividade_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @ManyToOne
    @JoinColumn(name = "periodo_id", nullable = false)
    private DimPeriodo dimPeriodo;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private DimStatus dimStatus;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private DimTipo dimTipo;

    @Column(name = "atividade_quantidade", precision = 10, scale = 2)
    private BigDecimal quantidade;
}