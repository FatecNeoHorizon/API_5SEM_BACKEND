// src/main/java/com/neohorizon/api/repository/FatoCustoHoraRepository.java
package com.neohorizon.api.repository.fato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neohorizon.api.entity.dimensao.DimDev;
import com.neohorizon.api.entity.dimensao.DimPeriodo;
import com.neohorizon.api.entity.dimensao.DimProjeto;
import com.neohorizon.api.entity.fato.FatoCustoHora;

public interface FatoCustoHoraRepository extends JpaRepository<FatoCustoHora, Long> {

       @Query("SELECT f FROM FatoCustoHora f WHERE (:dimProjeto is null or f.dimProjeto = :dimProjeto) and (:dimPeriodo is null"
                     + " or f.dimPeriodo = :dimPeriodo) and (:dimDev is null or f.dimDev = :dimDev)")
       public List<FatoCustoHora> findByDimProjetoAndDimPeriodoAndDimDev(@Param("dimProjeto") DimProjeto dimProjeto,
                     @Param("dimPeriodo") DimPeriodo dimPeriodo, @Param("dimDev") DimDev dimDev);

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
