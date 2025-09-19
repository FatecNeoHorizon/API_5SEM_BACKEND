package com.neohorizon.api.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String jira_id;
}
