package com.neohorizon.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fato_issue")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FatoIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private DimProjeto dimProjeto;

    @ManyToOne
    @JoinColumn(name = "periodo_id", nullable = false)
    private DimPeriodo dimPeriodo;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private DimStatus dimStatus;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private DimTipo dimTipo;

    @Column(name = "issue_quantidade")
    private Integer quantidade;
}
