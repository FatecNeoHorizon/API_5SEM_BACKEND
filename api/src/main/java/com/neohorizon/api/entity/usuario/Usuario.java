package com.neohorizon.api.entity.usuario;

import com.neohorizon.api.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="usuario")
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable=false)
    private Long usuario_id;

    @Column(name="email", nullable=false, unique=true)
    private String email;

    @Column(name="senha", nullable=false)
    private String senha;

    @Column(name="cargo", nullable=false)
    @Enumerated(EnumType.STRING)
    private RoleType cargo;

}
