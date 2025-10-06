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
@Table(name = "dim_dev")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimDev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dev_id")
    private Long id;

    @NonNull
    @Column(name = "dev_nome")
    private String nome;

    @Column(name = "dev_email")
    private String email;

    @Column(name = "dev_senha")
    private String senha;

    @Column(name = "dev_role")
    private String role;

    @Column(name = "dev_custo_hora")
    private Integer custo_hora;

    @Column(name = "dev_ativo")
    private Boolean ativo;
}
