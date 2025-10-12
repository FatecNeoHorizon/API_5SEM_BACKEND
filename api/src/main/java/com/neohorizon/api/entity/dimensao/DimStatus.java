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
@Table(name = "dim_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @NonNull
    @Column(name = "status_nome")
    private String nome;

    @Column(name = "status_jira_id")
    private String statusJiraId;
}
