package com.neohorizon.api.entity.fato;

import java.time.LocalDateTime;

import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.dimensao.DimTipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fato_apontamento_horas",
       indexes = {
           @Index(name = "idx_apontamento_dev_periodo", columnList = "dev_id, periodo_id"),
           @Index(name = "idx_apontamento_projeto_periodo", columnList = "projeto_id, periodo_id")
       }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoApontamentoHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apontamento_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "periodo_id", nullable = false)
    private DimPeriodo dimPeriodo;

    @ManyToOne
    @JoinColumn(name = "dev_id", nullable = false)
    private DimDev dimDev;

    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private DimAtividade dimAtividade;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private DimTipo dimTipo;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @Column(name = "horas_trabalhadas")
    private Double horasTrabalhadas;

    @Column(name = "descricao_trabalho")
    private String descricaoTrabalho;

    @Column(name = "data_criacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}