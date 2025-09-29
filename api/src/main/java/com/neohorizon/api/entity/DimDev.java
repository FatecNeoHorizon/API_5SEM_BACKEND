package com.neohorizon.api.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
