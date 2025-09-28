package com.neohorizon.api.repository;

import com.neohorizon.api.entity.DimAtividade;
import com.neohorizon.api.entity.DimProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimAtividadeRepository extends JpaRepository<DimAtividade, Long> {

    List<DimAtividade> findByDimProjetoAndAtivoTrue(DimProjeto projeto);
    
    List<DimAtividade> findByAtivoTrue();
    
    @Query("SELECT a FROM DimAtividade a WHERE a.dimProjeto.id = :projetoId AND a.ativo = true")
    List<DimAtividade> findByProjetoIdAndAtivoTrue(@Param("projetoId") Long projetoId);
}
