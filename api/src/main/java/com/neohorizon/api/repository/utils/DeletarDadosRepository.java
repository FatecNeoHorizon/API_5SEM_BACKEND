package com.neohorizon.api.repository.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.neohorizon.api.entity.dimensao.DimPeriodo;

import jakarta.transaction.Transactional;

public interface DeletarDadosRepository extends JpaRepository<DimPeriodo, Long>{

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE fato_apontamento_horas, fato_custo_hora, fato_atividade, dim_atividade, dim_periodo, dim_projeto, dim_status, dim_tipo RESTART IDENTITY CASCADE", nativeQuery = true)
       public void truncateTables();

}
