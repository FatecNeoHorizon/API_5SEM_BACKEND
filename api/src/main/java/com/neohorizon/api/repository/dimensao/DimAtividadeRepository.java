package com.neohorizon.api.repository.dimensao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neohorizon.api.entity.dimensao.DimAtividade;

public interface DimAtividadeRepository extends JpaRepository<DimAtividade, Long> {
    
    List<DimAtividade> findByAtivoTrue();

}
