package com.neohorizon.api.repository.fato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neohorizon.api.entity.dimensao.DimAtividade;
import com.neohorizon.api.entity.fato.FatoApontamentoHoras;

@Repository
public interface FatoApontamentoHorasRepository extends JpaRepository<FatoApontamentoHoras, Long> {

    // Buscar por desenvolvedor em um período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dataCriacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dataCriacao DESC, f.dimAtividade.nome")
    List<FatoApontamentoHoras> findByDevAndPeriodo(
        @Param("devId") Long devId,
        @Param("dataInicio") java.time.LocalDateTime dataInicio,
        @Param("dataFim") java.time.LocalDateTime dataFim
    );

    // Buscar por desenvolvedor, atividade e período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dimAtividade.id = :atividadeId " +
           "AND f.dataCriacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dataCriacao DESC")
    List<FatoApontamentoHoras> findByDevAtividadeAndPeriodo(
        @Param("devId") Long devId,
        @Param("atividadeId") Long atividadeId,
        @Param("dataInicio") java.time.LocalDateTime dataInicio,
        @Param("dataFim") java.time.LocalDateTime dataFim
    );

    // Buscar todos em um período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE " +
           "f.dataCriacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dimDev.nome, f.dataCriacao DESC, f.dimAtividade.nome")
    List<FatoApontamentoHoras> findByPeriodo(
        @Param("dataInicio") java.time.LocalDateTime dataInicio,
        @Param("dataFim") java.time.LocalDateTime dataFim
    );

    // Buscar por atividade e período
    @Query("SELECT f FROM FatoApontamentoHoras f WHERE f.dimAtividade.id = :atividadeId " +
           "AND f.dataCriacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY f.dimDev.nome, f.dataCriacao DESC")
    List<FatoApontamentoHoras> findByAtividadeAndPeriodo(
        @Param("atividadeId") Long atividadeId,
        @Param("dataInicio") java.time.LocalDateTime dataInicio,
        @Param("dataFim") java.time.LocalDateTime dataFim
    );


    // Buscar apontamentos por atividade e data específica
    List<FatoApontamentoHoras> findByDimAtividadeAndDataCriacao(DimAtividade atividade, java.time.LocalDateTime data);

    // Somatório de horas por desenvolvedor em um período
    @Query("SELECT SUM(f.horasTrabalhadas) FROM FatoApontamentoHoras f WHERE f.dimDev.id = :devId " +
           "AND f.dataCriacao BETWEEN :dataInicio AND :dataFim")
    Double sumHorasByDevAndPeriodo(
        @Param("devId") Long devId,
        @Param("dataInicio") java.time.LocalDateTime dataInicio,
        @Param("dataFim") java.time.LocalDateTime dataFim
    );
}
