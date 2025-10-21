package com.neohorizon.api.repository.dimensao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.neohorizon.api.entity.dimensao.DimPeriodo;

public interface DimPeriodoRepository extends JpaRepository<DimPeriodo, Long>  {

    @Query("SELECT f FROM DimPeriodo f WHERE (:dia is null or f.dia = :dia) and (:semana is null"
                     + " or f.semana = :semana) and (:mes is null or f.mes = :mes) and (:ano is null or f.ano = :ano)")
       public List<DimPeriodo> findByDiaAndSemanaAndMesAndAno(@Param("dia") Integer dia,
                     @Param("semana") Integer semana, @Param("mes") Integer mes, @Param("ano") Integer ano);
}
