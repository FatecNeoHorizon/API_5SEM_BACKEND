package com.neohorizon.api.repository.dimensao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neohorizon.api.entity.dimensao.DimTipo;

public interface DimTipoRepository extends JpaRepository<DimTipo, Long>  {
    List<DimTipo> findByNome(String nome);
}
