package com.neohorizon.api.repository.dimensao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.dimensao.DimProjeto;

public interface DimAtividadeRepository extends JpaRepository<DimAtividade, Long> {

    List<DimAtividade> findByDimProjetoAndAtivoTrue(DimProjeto projeto);
    
    List<DimAtividade> findByAtivoTrue();
    
    @Query("SELECT a FROM DimAtividade a WHERE a.dimProjeto.id = :projetoId AND a.ativo = true")
    List<DimAtividade> findByProjetoIdAndAtivoTrue(@Param("projetoId") Long projetoId);

}
