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
@Table(name = "dim_projeto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projeto_id")
    private Long id;

    @NonNull
    @Column(name = "projeto_nome")
    private String nome;

    @NonNull
    @Column(name = "projeto_key", unique = true)
    private String key;

    @Column(name = "projeto_jira_id")
    private String projeto_jira_id;
}
