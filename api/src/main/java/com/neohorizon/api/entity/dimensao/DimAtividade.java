package com.neohorizon.api.entity.dimensao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @Column(name = "atividade_jira_id")
    private String atividade_jira_id;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
