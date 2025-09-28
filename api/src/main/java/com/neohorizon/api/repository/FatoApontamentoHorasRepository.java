package com.neohorizon.api.repository;

import com.neohorizon.api.entity.FatoApontamentoHoras;
import com.neohorizon.api.entity.DimDev;
import com.neohorizon.api.entity.DimAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FatoApontamentoHorasRepository extends JpaRepository<FatoApontamentoHoras, Long> {

    // Buscar por desenvolvedor em um período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dataApontamento BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dataApontamento DESC, f.dimAtividade.nome")
    List<FatoApontamentoHoras> findByDevAndPeriodo(
        @Param("devId") Long devId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    // Buscar por desenvolvedor, atividade e período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dimAtividade.id = :atividadeId " +
           "AND f.dataApontamento BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dataApontamento DESC")
    List<FatoApontamentoHoras> findByDevAtividadeAndPeriodo(
        @Param("devId") Long devId,
        @Param("atividadeId") Long atividadeId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    // Buscar todos em um período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE " +
           "f.dataApontamento BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dimDev.nome, f.dataApontamento DESC, f.dimAtividade.nome")
    List<FatoApontamentoHoras> findByPeriodo(
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    // Buscar por atividade e período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimAtividade.id = :atividadeId " +
           "AND f.dataApontamento BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dimDev.nome, f.dataApontamento DESC")
    List<FatoApontamentoHoras> findByAtividadeAndPeriodo(
        @Param("atividadeId") Long atividadeId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );

    // Buscar apontamentos por desenvolvedor e data específica
    List<FatoApontamentoHoras> findByDimDevAndDataApontamento(DimDev dev, LocalDate data);

    // Buscar apontamentos por atividade e data específica
    List<FatoApontamentoHoras> findByDimAtividadeAndDataApontamento(DimAtividade atividade, LocalDate data);

    // Somatório de horas por desenvolvedor em um período
    @Query("SELECT SUM(f.horasTrabalhadas) FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dataApontamento BETWEEN :dataInicio AND :dataFim")
    Double sumHorasByDevAndPeriodo(
        @Param("devId") Long devId,
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
}
