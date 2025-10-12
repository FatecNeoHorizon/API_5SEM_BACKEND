package com.neohorizon.api.repository.fato;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neohorizon.api.dto.metrica.AtividadeAggregationDTO;
import com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO;
import com.neohorizon.api.entity.fato.FatoAtividade;

public interface FatoAtividadeRepository extends JpaRepository<FatoAtividade, Long> {

    @Query("SELECT COALESCE(SUM(fa.quantidade), 0) FROM FatoAtividade fa")
    Long countAllAtividades();

    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(dp.nome, SUM(fa.quantidade)) " +
        "FROM FatoAtividade fa JOIN fa.dimProjeto dp " +
        "WHERE dp.id = :projectId " +
        "GROUP BY dp.nome")
    List<ProjectAtividadeCountDTO> findAtividadeByProject(@Param("projectId") Long projectId);

    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(dp.nome, SUM(fa.quantidade)) " +
        "FROM FatoAtividade fa JOIN fa.dimProjeto dp " +
        "GROUP BY dp.nome")
    List<ProjectAtividadeCountDTO> findAllProjectAtividades();

    // Agregações por período - substituindo SQL nativo por JPQL
    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(" +
           // Label: ano-mes-dia (sem zero padding para compatibilidade)
           "CONCAT(CONCAT(CONCAT(CAST(dp.ano AS string), '-'), CONCAT(CAST(dp.mes AS string), '-')), CAST(dp.dia AS string)), " +
           "SUM(fa.quantidade)) " +
           "FROM FatoAtividade fa JOIN fa.dimPeriodo dp " +
           "WHERE (dp.ano > :anoInicio OR (dp.ano = :anoInicio AND (dp.mes > :mesInicio OR (dp.mes = :mesInicio AND dp.dia >= :diaInicio)))) " +
           "AND (dp.ano < :anoFim OR (dp.ano = :anoFim AND (dp.mes < :mesFim OR (dp.mes = :mesFim AND dp.dia <= :diaFim)))) " +
           "GROUP BY dp.ano, dp.mes, dp.dia " +
           "ORDER BY dp.ano, dp.mes, dp.dia")
    List<ProjectAtividadeCountDTO> findAtividadesByDia(
        @Param("anoInicio") Integer anoInicio, @Param("mesInicio") Integer mesInicio, @Param("diaInicio") Integer diaInicio,
        @Param("anoFim") Integer anoFim, @Param("mesFim") Integer mesFim, @Param("diaFim") Integer diaFim);

    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(" +
           // Label: ano-Ssemana (sem zero padding)
           "CONCAT(CONCAT(CAST(dp.ano AS string), '-S'), CAST(dp.semana AS string)), " +
           "SUM(fa.quantidade)) " +
           "FROM FatoAtividade fa JOIN fa.dimPeriodo dp " +
           "WHERE (dp.ano > :anoInicio OR (dp.ano = :anoInicio AND dp.semana >= :semanaInicio)) " +
           "AND (dp.ano < :anoFim OR (dp.ano = :anoFim AND dp.semana <= :semanaFim)) " +
           "GROUP BY dp.ano, dp.semana " +
           "ORDER BY dp.ano, dp.semana")
    List<ProjectAtividadeCountDTO> findAtividadesBySemana(
        @Param("anoInicio") Integer anoInicio, @Param("semanaInicio") Integer semanaInicio,
        @Param("anoFim") Integer anoFim, @Param("semanaFim") Integer semanaFim);

    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(" +
           // Label: ano-mes (sem zero padding)
           "CONCAT(CONCAT(CAST(dp.ano AS string), '-'), CAST(dp.mes AS string)), " +
           "SUM(fa.quantidade)) " +
           "FROM FatoAtividade fa JOIN fa.dimPeriodo dp " +
           "WHERE (dp.ano > :anoInicio OR (dp.ano = :anoInicio AND dp.mes >= :mesInicio)) " +
           "AND (dp.ano < :anoFim OR (dp.ano = :anoFim AND dp.mes <= :mesFim)) " +
           "GROUP BY dp.ano, dp.mes " +
           "ORDER BY dp.ano, dp.mes")
    List<ProjectAtividadeCountDTO> findAtividadesByMes(
        @Param("anoInicio") Integer anoInicio, @Param("mesInicio") Integer mesInicio,
        @Param("anoFim") Integer anoFim, @Param("mesFim") Integer mesFim);

    @Query("SELECT new com.neohorizon.api.dto.metrica.ProjectAtividadeCountDTO(" +
           "CAST(dp.ano AS string), " +
           "SUM(fa.quantidade)) " +
           "FROM FatoAtividade fa JOIN fa.dimPeriodo dp " +
           "WHERE dp.ano BETWEEN :anoInicio AND :anoFim " +
           "GROUP BY dp.ano " +
           "ORDER BY dp.ano")
    List<ProjectAtividadeCountDTO> findAtividadesByAno(
        @Param("anoInicio") Integer anoInicio, @Param("anoFim") Integer anoFim);

    
    
    // Método unificado usando DTO de agregação (seguindo padrão antigo)
    default List<ProjectAtividadeCountDTO> findAtividadesByPeriod(AtividadeAggregationDTO aggregationDTO) {
        return switch (aggregationDTO.getPeriodo()) {
            case DIA -> findAtividadesByDia(
                aggregationDTO.getAnoInicio(), aggregationDTO.getMesInicio(), aggregationDTO.getDiaInicio(),
                aggregationDTO.getAnoFim(), aggregationDTO.getMesFim(), aggregationDTO.getDiaFim());
            case SEMANA -> {
                // Calcular semana baseado nos dados de início e fim
                Integer semanaInicio = (aggregationDTO.getMesInicio() - 1) * 4 + (aggregationDTO.getDiaInicio() / 7) + 1;
                Integer semanaFim = (aggregationDTO.getMesFim() - 1) * 4 + (aggregationDTO.getDiaFim() / 7) + 1;
                yield findAtividadesBySemana(
                    aggregationDTO.getAnoInicio(), semanaInicio,
                    aggregationDTO.getAnoFim(), semanaFim);
            }
            case MES -> findAtividadesByMes(
                aggregationDTO.getAnoInicio(), aggregationDTO.getMesInicio(),
                aggregationDTO.getAnoFim(), aggregationDTO.getMesFim());
            case ANO -> findAtividadesByAno(
                aggregationDTO.getAnoInicio(), aggregationDTO.getAnoFim());
        };
    }
    
}