package com.neohorizon.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_entity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String description;
}