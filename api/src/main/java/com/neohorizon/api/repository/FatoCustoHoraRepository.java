// src/main/java/com/neohorizon/api/repository/FatoCustoHoraRepository.java
package com.neohorizon.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.neohorizon.api.entity.FatoCustoHora;

public interface FatoCustoHoraRepository extends JpaRepository<FatoCustoHora, Long> {

    @Query("select coalesce(sum(f.custo),0) from FatoCustoHora f")
    Long totalGeral();

    @Query("""
           select f.dimProjeto.id, f.dimProjeto.nome, coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimProjeto.id, f.dimProjeto.nome
           order by coalesce(sum(f.custo),0) desc
           """)
    List<Object[]> totalPorProjetoRaw();

    @Query("""
           select f.dimDev.id, f.dimDev.nome, coalesce(sum(f.horas_quantidade),0), coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimDev.id, f.dimDev.nome
           order by coalesce(sum(f.custo),0) desc
           """)
    List<Object[]> totalPorDevRaw();

    @Query("""
           select f.dimPeriodo.ano, f.dimPeriodo.mes, coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimPeriodo.ano, f.dimPeriodo.mes
           order by f.dimPeriodo.ano, f.dimPeriodo.mes
           """)
    List<Object[]> evolucaoMesRaw();

    @Query("""
           select f.dimPeriodo.ano, coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimPeriodo.ano
           order by f.dimPeriodo.ano
           """)
    List<Object[]> evolucaoAnoRaw();

    @Query("""
           select f.dimPeriodo.ano, f.dimPeriodo.semana, coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimPeriodo.ano, f.dimPeriodo.semana
           order by f.dimPeriodo.ano, f.dimPeriodo.semana
           """)
    List<Object[]> evolucaoSemanaRaw();

    @Query("""
           select f.dimPeriodo.ano, f.dimPeriodo.mes, f.dimPeriodo.dia, coalesce(sum(f.custo),0)
           from FatoCustoHora f
           group by f.dimPeriodo.ano, f.dimPeriodo.mes, f.dimPeriodo.dia
           order by f.dimPeriodo.ano, f.dimPeriodo.mes, f.dimPeriodo.dia
           """)
    List<Object[]> evolucaoDiaRaw();
}
