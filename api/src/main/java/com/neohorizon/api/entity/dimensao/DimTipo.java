package com.neohorizon.api.entity.dimensao;

import jakarta.persistence.*;
import lombok.*;

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

}
