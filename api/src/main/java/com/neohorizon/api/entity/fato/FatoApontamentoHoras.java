package com.neohorizon.api.entity.fato;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimProjeto;

@Entity
@Table(name = "fato_apontamento_horas")
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
    @JoinColumn(name = "dev_id", nullable = false)
    private DimDev dimDev;

    @ManyToOne
    @JoinColumn(name = "atividade_id", nullable = false)
    private DimAtividade dimAtividade;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @Column(name = "data_apontamento", nullable = false)
    private LocalDate dataApontamento;

    @Column(name = "horas_trabalhadas", nullable = false)
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
