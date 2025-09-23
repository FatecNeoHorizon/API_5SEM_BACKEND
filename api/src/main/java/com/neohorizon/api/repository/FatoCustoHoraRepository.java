package com.neohorizon.api.repository;

import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimPeriodo;
import com.neohorizon.api.entity.DimProjeto;
import com.neohorizon.api.entity.FatoCustoHora;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FatoCustoHoraRepository extends JpaRepository<FatoCustoHora, Long>  {

    @Query("SELECT f FROM FatoCustoHora f WHERE (:dimProjeto is null or f.dimProjeto = :dimProjeto) and (:dimPeriodo is null"
  + " or f.dimPeriodo = :dimPeriodo) and (:dimDev is null or f.dimDev = :dimDev)")
    public List<FatoCustoHora> findByDimProjetoAndDimPeriodoAndDimDev(@Param("dimProjeto") DimProjeto dimProjeto, @Param("dimPeriodo") DimPeriodo dimPeriodo, @Param("dimDev") DimDev dimDev);
}
