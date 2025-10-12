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
@Table(name = "dim_tipo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_id")
    private Long id;

    @NonNull
    @Column(name = "tipo_nome")
    private String nome;

    @Column(name = "tipo_descricao")
    private String descricao;

    @Column(name = "tipo_jira_id")
    private String tipoJiraId;

}
