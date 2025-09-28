package com.neohorizon.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dim_atividade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimAtividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "atividade_id")
    private Long id;

    @NonNull
    @Column(name = "atividade_nome")
    private String nome;

    @Column(name = "atividade_descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
