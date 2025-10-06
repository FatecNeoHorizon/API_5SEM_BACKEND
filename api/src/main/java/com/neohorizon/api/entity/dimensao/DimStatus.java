package com.neohorizon.api.entity.dimensao;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "status_descricao")
    private String descricao;
}
